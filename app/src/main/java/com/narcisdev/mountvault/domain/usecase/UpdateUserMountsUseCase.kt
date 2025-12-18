package com.narcisdev.mountvault.domain.usecase

import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.repository.AuthRepository
import com.narcisdev.mountvault.domain.repository.UserRepository
import javax.inject.Inject

data class UpdateUserMountsUseCase @Inject constructor(
    private val repository: AuthRepository, private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserEntity) {
        userRepository.updateUserLocal({ currentUser ->
            currentUser.copy(
                ownedCards = currentUser.ownedCards + user.ownedCards
            )
        })
        userRepository.updateFirebaseUserMounts(user)
    }
}
