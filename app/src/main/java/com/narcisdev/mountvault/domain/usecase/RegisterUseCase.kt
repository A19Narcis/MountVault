package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(user: UserEntity, password: String): UserEntity? {
        return repository.registerUser(user = user, password = password)
    }

}
