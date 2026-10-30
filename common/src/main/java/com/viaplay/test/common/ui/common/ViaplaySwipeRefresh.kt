package com.viaplay.test.common.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.viaplay.test.common.base.BaseScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <TYPE, STATE : BaseScreenState<TYPE, STATE>> ViaplaySwipeRefresh(
    modifier: Modifier = Modifier,
    state: STATE,
    isRefreshing: Boolean = state.base.isRefreshing,
    refresh: () -> Unit,
    mainContent: @Composable () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        mainContent()
    }
}
