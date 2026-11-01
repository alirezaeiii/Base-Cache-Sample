package com.viaplay.test.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viaplay.test.domain.repository.BaseRepository
import com.viaplay.test.domain.utils.Async
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<DataType, STATE : BaseScreenState<DataType, STATE>, QueryType, FetchType>(
    private val repository: BaseRepository<DataType, QueryType, FetchType>,
    initialState: STATE,
    private val queryParam: QueryType? = null,
    private val fetchParam: FetchType? = null,
    loadDataOnInit: Boolean = true
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var job: Job? = null

    init {
        if (loadDataOnInit) {
            refresh()
        }
    }

    protected open fun onSuccess(items: DataType) {
        updateState { old ->
            old.withSuccess(items)
        }
    }

    protected fun updateState(reducer: (STATE) -> STATE) {
        _state.update(reducer)
    }

    fun refresh(
        forceRefresh: Boolean = true
    ) {
        job?.cancel()
        job = repository.getResult(queryParam, fetchParam, forceRefresh).onEach { uiState ->
            when (uiState) {
                is Async.Loading -> {
                    updateState { old ->
                        reduceLoading(old, uiState.isRefreshing)
                    }
                }

                is Async.Success -> onSuccess(uiState.data)

                is Async.Error -> {
                    updateState { old ->
                        reduceError(old, uiState.message, uiState.isWarning)
                    }
                    if (uiState.isWarning) {
                        emitWarning(uiState.message)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun reduceLoading(old: STATE, isRefreshing: Boolean): STATE =
        old.withLoading(isRefreshing)

    private fun reduceError(old: STATE, msg: String, isWarning: Boolean): STATE =
        old.withError(msg, isWarning)

    private suspend fun emitWarning(message: String) {
        _uiEvent.emit(UiEvent.ShowWarning(message))
    }

    protected fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
