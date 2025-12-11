package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchUserRemote(uid: String): UserEntity?
    suspend fun saveUserLocal(user: UserEntity)
    fun getUserLocal(): Flow<UserEntity>
    suspend fun updateUserLocal(update: (UserEntity) -> UserEntity)
    suspend fun removeUserLocal()
    suspend fun updateFirebaseUser(user: UserEntity)
}