package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.MountEntity
import kotlinx.coroutines.flow.Flow

interface MountRepository {
    fun startMountsSync()

    fun observeAllMounts(): Flow<List<MountEntity>>

    fun observeCollectedMounts(ids: List<String>): Flow<List<MountEntity>>
}