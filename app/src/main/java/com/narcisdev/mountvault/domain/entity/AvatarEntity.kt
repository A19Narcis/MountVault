package com.narcisdev.mountvault.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "avatars")
data class AvatarEntity(
    @PrimaryKey
    val url: String
)
