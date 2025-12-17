package com.narcisdev.mountvault.domain.entity

data class PackEntity(
    val id: Int,
    val name: String,
    val commonDropChange: String,
    val rareDropChange: String,
    val epicDropChange: String,
    val legendaryDropChange: String,
)