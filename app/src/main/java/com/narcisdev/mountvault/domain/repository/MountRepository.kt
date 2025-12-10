package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.MountEntity

interface MountRepository {

    suspend fun getMountsFromFirebaseToRoom()

    suspend fun getAllMountsFromRoom(): List<MountEntity>

    suspend fun getCollectedMountsFromUser(ids: List<String>): List<MountEntity>

}