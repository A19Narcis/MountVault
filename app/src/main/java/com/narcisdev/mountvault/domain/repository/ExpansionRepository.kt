package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.ExpansionEntity

interface ExpansionRepository {
    suspend fun getExpansionsFromFirebaseToRoom()

    suspend fun getAllExpansionsFromRoom(): List<ExpansionEntity>
}