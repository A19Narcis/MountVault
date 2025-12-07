package com.narcisdev.mountvault.domain.repository

import com.narcisdev.mountvault.domain.entity.UserEntity

interface AuthRepository {
    suspend fun loginWithEmail(email:String, password:String): UserEntity?
    suspend fun getEmailByUsername(username:String): String?
    suspend fun registerUser(user: UserEntity, password: String): UserEntity?

    suspend fun logout()

}