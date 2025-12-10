package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.repository.UserRepository
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore, private val local: UserPreferencesDataSource
) : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun fetchUserRemote(uid: String): UserEntity? {
        return try {
            val snap = db.collection("users").document().get().await()

            snap.toObject(UserEntity::class.java)?.also {
                saveUserLocal(it)
            }
        } catch (e: Exception) {
            Log.i(Constants.APP_NAME, "Error fetching user: ${e.message}")
            return null
        }
    }

    override suspend fun saveUserLocal(user: UserEntity) {
        local.saveUser(user)
    }

    override fun getUserLocal(): Flow<UserEntity> = local.userFlow.filterNotNull()

    override suspend fun updateUserLocal(update: (UserEntity) -> UserEntity) {
        local.updateUser(update)
    }

    override suspend fun removeUserLocal() {
        local.clear()
    }
}