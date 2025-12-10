package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class GetUserMountsUseCase @Inject constructor(private val repository: MountRepository) {
    suspend operator fun invoke(ids: List<String>): List<MountEntity> {
        return repository.getCollectedMountsFromUser(ids)
    }
}