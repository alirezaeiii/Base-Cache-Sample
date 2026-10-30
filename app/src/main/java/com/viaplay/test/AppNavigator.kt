package com.viaplay.test

import androidx.navigation3.runtime.NavKey

class AppNavigator(val state: NavigationState) {
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // Top-level route -> switch stack
            state.topLevelRoute = route
        } else {
            // Push onto current stack
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return
        val currentRoute = currentStack.last()
        if (currentRoute == state.topLevelRoute) {
            // At root of a top-level stack -> return to start
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}