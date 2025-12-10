package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class SyncMountsUseCase @Inject constructor(private val repository: MountRepository) {
    suspend operator fun invoke() {
        repository.getMountsFromFirebaseToRoom()
    }
}
