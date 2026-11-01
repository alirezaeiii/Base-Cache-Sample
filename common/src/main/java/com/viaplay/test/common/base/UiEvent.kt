package com.viaplay.test.common.base

import androidx.navigation3.runtime.NavKey

sealed interface UiEvent {
    data class ShowWarning(val message: String) : UiEvent
    data class Navigate(val route: NavKey) : UiEvent
    data object NavigateUp : UiEvent
}
