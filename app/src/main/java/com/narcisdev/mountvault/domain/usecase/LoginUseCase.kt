package com.narcisdev.mountvault.domain.usecase

import android.util.Log
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(identifier: String, password: String): UserEntity? {
        return if (identifier.contains("@")) {
            repository.loginWithEmail(email = identifier, password = password)
        } else {
            val email = repository.getEmailByUsername(username = identifier) ?: return null
            repository.loginWithEmail(email = email, password = password)
        }
    }

}
