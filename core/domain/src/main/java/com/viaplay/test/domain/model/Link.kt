package com.viaplay.test.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val id: String,
    val title: String,
    val href: String
)