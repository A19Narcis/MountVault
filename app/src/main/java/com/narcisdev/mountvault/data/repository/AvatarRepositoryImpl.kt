package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.dao.AvatarsDao
import com.narcisdev.mountvault.data.dao.ExpansionDao
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.AvatarRepository
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AvatarRepositoryImpl @Inject constructor (
    private val avatarsDao: AvatarsDao
) : AvatarRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getAvatarsFromFirebaseToRoom() {
        try {
            val snapshot = firestore.collection("avatars").get().await()
            val avatars = snapshot.map { doc ->
                AvatarEntity(url = doc.getString("url") ?: "No_Id")
            }
            avatarsDao.insertAll(avatars)
        } catch (e: Exception) {
            Log.e(Constants.APP_NAME, "Error loading mounts", e)
        }
    }

    override suspend fun getAllAvatarsFromRoom(): List<AvatarEntity> {
        return avatarsDao.getAllAvatars()
    }

}