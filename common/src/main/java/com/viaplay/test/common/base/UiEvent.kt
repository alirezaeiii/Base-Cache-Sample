package com.viaplay.test.common.base

import androidx.navigation3.runtime.NavKey

interface UiEvent {
    interface Warning : UiEvent {
        val message: String
    }
    interface Navigation : UiEvent {
        val route: NavKey
    }
    interface NavigateUp : UiEvent
}
