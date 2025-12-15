package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.repository.MountRepository
import javax.inject.Inject

class StartMountsSyncUseCase @Inject constructor(private val repository: MountRepository) {
    operator fun invoke() {
        repository.startMountsSync()
    }
}
