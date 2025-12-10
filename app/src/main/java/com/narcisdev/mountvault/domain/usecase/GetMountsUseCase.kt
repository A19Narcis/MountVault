package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class GetMountsUseCase @Inject constructor(private val repository: MountRepository) {
    suspend operator fun invoke(): List<MountEntity> {
        return repository.getAllMountsFromRoom()
    }
}