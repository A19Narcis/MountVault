package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.dao.ExpansionDao
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExpansionRepositoryImpl @Inject constructor (
    private val expansionDao: ExpansionDao
) : ExpansionRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getExpansionsFromFirebaseToRoom() {
        try {
            val snapshot = firestore.collection("expansions").orderBy("year").get().await()
            val expansions = snapshot.map { doc ->
                ExpansionEntity(
                    id = doc.getString("id") ?: "No_Id",
                    name = doc.getString("name")?: "No_Name",
                    coverUrl = doc.getString("coverUrl")?: "No_ImageUrl",
                    year = doc.getString("year")?: "No_Cost",

                )
            }
            expansionDao.insertAll(expansions)
        } catch (e: Exception) {
            Log.e(Constants.APP_NAME, "Error loading mounts", e)
        }
    }

    override suspend fun getAllExpansionsFromRoom(): List<ExpansionEntity> {
        return expansionDao.getAllExpansions()
    }

}