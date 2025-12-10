package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MountRepositoryImpl @Inject constructor (
    private val mountDao: MountDao
) : MountRepository {
    private val firestore = FirebaseFirestore.getInstance()


    override suspend fun getMountsFromFirebaseToRoom() {
        // Get collection from Firebase
        try {
            val snapshot = firestore.collection("mounts").get().await()
            val mounts = snapshot.map { doc ->
                MountEntity(
                    id = doc.getString("id") ?: "No_Id",
                    name = doc.getString("name")?: "No_Name",
                    faction = doc.getString("faction")?: "No_Description",
                    imageUrl = doc.getString("imageUrl")?: "No_ImageUrl",
                    rarity = doc.getString("rarity")?: "No_Rarity",
                    cost = doc.getString("cost")?: "No_Cost",
                    dropRate = (doc.getLong("dropRate") ?: 0.0).toDouble(),
                    expansionId = doc.getString("expansionId") ?: "No_ExpansionId",
                    source = doc.getString("source")?: "No_Source",
                    type = doc.getString("type")?: "No_Type",
                )
            }
            mountDao.insertAll(mounts)
        } catch (e: Exception) {
            Log.e(Constants.APP_NAME, "Error loading mounts", e)
        }
    }

    override suspend fun getAllMountsFromRoom(): List<MountEntity> {
        return mountDao.getAllMounts()
    }

    override suspend fun getCollectedMountsFromUser(ids: List<String>): List<MountEntity> {
        return mountDao.getAllMountsFromUser(ids)
    }

}