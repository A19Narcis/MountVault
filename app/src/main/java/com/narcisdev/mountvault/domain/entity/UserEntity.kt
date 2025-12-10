package com.narcisdev.mountvault.domain.entity

import com.google.firebase.Timestamp
import com.narcisdev.mountvault.core.components.Constants

data class UserEntity(
    val age: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val email: String = "",
    val ownedCards: List<String> = emptyList(),
    val userType: String = Constants.NORMAL_USER,
    val userUrl: String = Constants.DEFAULT_PROFILE_IMG,
    val username: String = "",
) {
    override fun toString(): String {
        return "UserEntity(age=$age, createdAt=$createdAt, email='$email', userType='$userType', userUrl='$userUrl', username='$username', ownedCards=$ownedCards)"
    }

}