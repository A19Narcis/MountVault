package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.repository.AuthRepository
import com.narcisdev.mountvault.core.components.Constants
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth, private val db: FirebaseFirestore
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): UserEntity? {
        try {
            auth.signOut()
            auth.signInWithEmailAndPassword(email, password).await()
            val uid = auth.currentUser?.uid ?: throw Exception(Constants.NOT_FOUND_USER)
            val document = db.collection("users").document(uid).get().await()

            if (!document.exists()) {
                throw Exception("User not found in database Firebase")
            }

            val user = document.toObject(UserEntity::class.java)?: throw Exception("Mapping error with user")

            return user
        } catch (e: Exception) {
            Log.i(Constants.APP_NAME, "Error: ${e.toString()}")
            return null
        }
    }

    override suspend fun getEmailByUsername(username: String): String? {
        return try {
            val snapshot = db.collection("usernames").document(username).get().await()
            snapshot.getString("email")
        } catch (e: Exception) {
            Log.i(Constants.APP_NAME, "Error getEmailByUsername: ${e.toString()}")
            null
        }
    }

    override suspend fun registerUser(
        user: UserEntity, password: String
    ): UserEntity? {
        try {
            val usernameSnapshot =
                db.collection("users").whereEqualTo("username", user.username).get().await()

            if (!usernameSnapshot.isEmpty) {
                return null
            }

            val emailSnapshot =
                db.collection("users").whereEqualTo("email", user.email).get().await()

            if (!emailSnapshot.isEmpty) {
                return null
            }

            auth.signOut()
            val userAuthRegistered = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = userAuthRegistered.user?.uid ?: throw Exception("Error registering user")

            db.collection("usernames").document(user.username).set(mapOf("email" to user.email))
                .await()

            val userData = mapOf(
                "age" to user.age,
                "createdAt" to user.createdAt,
                "email" to user.email,
                "ownedCards" to user.ownedCards,
                "userType" to user.userType,
                "userUrl" to user.userUrl,
                "username" to user.username

            )
            db.collection("users").document(uid).set(userData).await()

            return user
        } catch (e: Exception) {
            Log.i(Constants.APP_NAME, "Error: ${e.toString()}")
            return null
        }

    }

    override suspend fun logout() {
        auth.signOut()
    }
}