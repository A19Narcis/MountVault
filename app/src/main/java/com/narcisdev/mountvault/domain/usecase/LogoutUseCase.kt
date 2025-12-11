package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.repository.AuthRepository
import com.narcisdev.mountvault.domain.repository.UserRepository
import javax.inject.Inject

data class LogoutUseCase @Inject constructor(private val repository: AuthRepository, private val userRepository: UserRepository) {
    suspend operator fun invoke() {
        userRepository.removeUserLocal()
        repository.logout()
    }
}
