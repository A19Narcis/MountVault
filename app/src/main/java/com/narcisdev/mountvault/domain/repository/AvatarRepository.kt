package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.AvatarEntity

interface AvatarRepository {
    suspend fun getAvatarsFromFirebaseToRoom()

    suspend fun getAllAvatarsFromRoom(): List<AvatarEntity>
}