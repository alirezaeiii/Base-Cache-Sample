package com.viaplay.test.feature.details

import com.viaplay.test.common.base.BaseRepository
import com.viaplay.test.common.base.BaseViewModel
import com.viaplay.test.common.base.ViewState
import com.viaplay.test.common.utils.cleanHref
import com.viaplay.test.domain.model.Link
import com.viaplay.test.domain.model.Section
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = SectionViewModel.Factory::class)
class SectionViewModel @AssistedInject constructor(
    repository: BaseRepository<Section, String, String>,
    @Assisted val link: Link
) : BaseViewModel<Section, SectionViewState, String, String, DetailsUiEvent>(
    repository,
    SectionViewState(base = ViewState(isLoading = true)),
    DetailsUiEvent::ShowWarning,
    link.id,
    link.href.cleanHref()
) {
    fun onBackClick() {
        emitEvent(DetailsUiEvent.NavigateUp)
    }

    @AssistedFactory
    interface Factory {
        fun create(link: Link): SectionViewModel
    }
}
