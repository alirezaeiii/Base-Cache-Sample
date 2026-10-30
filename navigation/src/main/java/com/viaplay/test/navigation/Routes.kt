package com.viaplay.test.navigation

import androidx.navigation3.runtime.NavKey
import com.viaplay.test.domain.model.Link
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {

    @Serializable
    data object LinksRoute : Routes

    @Serializable
    data class DetailsRoute(val link: Link) : Routes
}
