package com.narcisdev.mountvault.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expansions")
data class ExpansionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val coverUrl: String,
    val mounts: List<String>,
    val totalMounts: String,
    val year: String
)
