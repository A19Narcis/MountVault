package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.repository.AvatarRepository
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class SyncAvatarsUseCase @Inject constructor(private val repository: AvatarRepository) {
    suspend operator fun invoke() {
        repository.getAvatarsFromFirebaseToRoom()
    }
}
