package com.narcisdev.mountvault.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mounts")
data class MountEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val faction: String,
    val imageUrl: String,
    val rarity: String,
    val cost: String,
    val dropRate: Double,
    val expansionId: String,
    val source: String,
    val type: String
)
