package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class SyncExpansionsUseCase @Inject constructor(private val repository: ExpansionRepository) {
    suspend operator fun invoke() {
        repository.getExpansionsFromFirebaseToRoom()
    }
}
