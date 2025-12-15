package com.narcisdev.mountvault.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "expansions")
@Serializable
data class ExpansionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val coverUrl: String,
    val mounts: List<String>,
    val totalMounts: String,
    val year: String
)
