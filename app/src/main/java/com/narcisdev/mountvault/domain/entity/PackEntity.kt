package com.narcisdev.mountvault.domain.entity

data class PackEntity(
    val id: Int,
    val name: String,
    val numberOfCards: Int,
    val commonDropChange: String,
    val rareDropChange: String,
    val epicDropChange: String,
    val legendaryDropChange: String,
)