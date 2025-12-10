package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import javax.inject.Inject

class GetExpansionsUseCase @Inject constructor(private val repository: ExpansionRepository) {
    suspend operator fun invoke(): List<ExpansionEntity> {
        return repository.getAllExpansionsFromRoom()
    }
}