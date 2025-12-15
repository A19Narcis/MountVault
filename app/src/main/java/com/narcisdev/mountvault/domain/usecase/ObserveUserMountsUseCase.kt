package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserMountsUseCase @Inject constructor(private val repository: MountRepository) {
    operator fun invoke(ids: List<String>): Flow<List<MountEntity>> = repository.observeCollectedMounts(ids)
}