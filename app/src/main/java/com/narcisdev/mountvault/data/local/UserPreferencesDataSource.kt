package com.narcisdev.mountvault.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.narcisdev.mountvault.domain.entity.UserEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataSource @Inject constructor (@ApplicationContext private val context: Context) {

    companion object {
        private val EMAIL = stringPreferencesKey("email")
        private val USERNAME = stringPreferencesKey("username")
        private val AGE = intPreferencesKey("age")
        private val USER_URL = stringPreferencesKey("userUrl")
        private val USER_TYPE = stringPreferencesKey("userType")
        private val OWNED_CARDS = stringSetPreferencesKey("ownedCards")
    }

    // Leer DataStore (Flow<UserData>)
    val userFlow: Flow<UserEntity?> = context.dataStore.data.map { prefs ->
        val email = prefs[EMAIL] ?: return@map null
        UserEntity(
            email = email,
            username = prefs[USERNAME] ?: "",
            age = prefs[AGE] ?: 0,
            userUrl = prefs[USER_URL] ?: "",
            userType = prefs[USER_TYPE] ?: "",
            ownedCards = prefs[OWNED_CARDS]?.toList() ?: emptyList()
        )
    }

    suspend fun getUserLocalOnce(): UserEntity? {
        return userFlow.firstOrNull()
    }


    // Guardar usuario completo
    suspend fun saveUser(user: UserEntity) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = user.email
            prefs[USERNAME] = user.username
            prefs[AGE] = user.age
            prefs[USER_URL] = user.userUrl
            prefs[USER_TYPE] = user.userType
            prefs[OWNED_CARDS] = user.ownedCards.toSet()
        }
    }

    // Actualizar usuario
    suspend fun updateUser(update: (UserEntity) -> UserEntity) {
        context.dataStore.edit { prefs ->
            val current = UserEntity(
                email = prefs[EMAIL] ?: "",
                username = prefs[USERNAME] ?: "",
                age = prefs[AGE] ?: 0,
                userUrl = prefs[USER_URL] ?: "",
                userType = prefs[USER_TYPE] ?: "",
                ownedCards = prefs[OWNED_CARDS]?.toList() ?: emptyList()
            )

            val newUser = update(current)

            prefs[EMAIL] = newUser.email
            prefs[USERNAME] = newUser.username
            prefs[AGE] = newUser.age
            prefs[USER_URL] = newUser.userUrl
            prefs[USER_TYPE] = newUser.userType
            prefs[OWNED_CARDS] = newUser.ownedCards.toSet()
        }
    }

    // Añadir una carta nueva al usuario
    suspend fun addOwnedCard(cardId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[OWNED_CARDS] ?: emptySet()
            prefs[OWNED_CARDS] = current + cardId
        }
    }

    // Limpiar datos al hacer logout
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

}