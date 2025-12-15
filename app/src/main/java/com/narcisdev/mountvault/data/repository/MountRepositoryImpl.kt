package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MountRepositoryImpl @Inject constructor(
    private val mountDao: MountDao
) : MountRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override fun startMountsSync() {

        firestore.collection("mounts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(Constants.APP_NAME, "Mounts listener error", error)
                    return@addSnapshotListener
                }

                val mounts = snapshot?.documents?.map { doc ->
                    MountEntity(
                        id = doc.getString("id") ?: "No_Id",
                        name = doc.getString("name") ?: "No_Name",
                        faction = doc.getString("faction") ?: "No_Description",
                        imageUrl = doc.getString("imageUrl") ?: "No_ImageUrl",
                        rarity = doc.getString("rarity") ?: "No_Rarity",
                        cost = doc.getString("cost") ?: "No_Cost",
                        dropRate = (doc.getLong("dropRate") ?: 0L).toDouble(),
                        expansionId = doc.getString("expansionId") ?: "No_ExpansionId",
                        source = doc.getString("source") ?: "No_Source",
                        type = doc.getString("type") ?: "No_Type",
                    )
                }.orEmpty()

                CoroutineScope(Dispatchers.IO).launch {
                    mountDao.insertAll(mounts)
                    Log.i(Constants.APP_NAME, "MOUNTS DE LA APP: $mounts")
                }
            }
    }

    override fun observeAllMounts(): Flow<List<MountEntity>> =
        mountDao.observeAllMounts()

    override fun observeCollectedMounts(ids: List<String>): Flow<List<MountEntity>> =
        mountDao.observeMountsFromUser(ids)
}