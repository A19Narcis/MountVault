package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.repository.AvatarRepository
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import javax.inject.Inject

class GetAvatarsUseCase @Inject constructor(private val repository: AvatarRepository) {
    suspend operator fun invoke(): List<AvatarEntity> {
        return repository.getAllAvatarsFromRoom()
    }
}