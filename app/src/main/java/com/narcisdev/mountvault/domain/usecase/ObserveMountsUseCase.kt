package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMountsUseCase @Inject constructor(private val repository: MountRepository) {
    operator fun invoke(): Flow<List<MountEntity>> = repository.observeAllMounts()
}