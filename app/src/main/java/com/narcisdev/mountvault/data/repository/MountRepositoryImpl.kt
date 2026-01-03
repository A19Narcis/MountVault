package com.narcisdev.mountvault.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.repository.MountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MountRepositoryImpl @Inject constructor(
    private val mountDao: MountDao
) : MountRepository {

    private val firestore = FirebaseFirestore.getInstance()

    @OptIn(DelicateCoroutinesApi::class)
    override fun startMountsSync() {

        firestore.collection("mounts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(Constants.APP_NAME, "Mounts listener error", error)
                    return@addSnapshotListener
                }

                val mounts = snapshot?.documents?.mapNotNull { doc ->
                    val id = doc.getString("id") ?: return@mapNotNull null
                    val name = doc.getString("name") ?: return@mapNotNull null

                    MountEntity(
                        id = id,
                        name = name,
                        faction = doc.getString("faction") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
                        rarity = doc.getString("rarity") ?: "",
                        cost = doc.getString("cost") ?: "",
                        dropRate = (doc.getDouble("dropRate") ?: 0.0f).toDouble(),
                        expansionId = doc.getString("expansionId") ?: "",
                        source = doc.getString("source") ?: "",
                        type = doc.getString("type") ?: ""
                    )
                }.orEmpty()

                GlobalScope.launch(Dispatchers.IO) {
                    mountDao.replaceAll(mounts)
                }

//                CoroutineScope(Dispatchers.IO).launch {
//                    mountDao.clearAll()
//                    mountDao.insertAll(mounts)
//                }
            }
    }

    override fun observeAllMounts(): Flow<List<MountEntity>> =
        mountDao.observeAllMounts()

    override fun observeCollectedMounts(ids: List<String>): Flow<List<MountEntity>> =
        mountDao.observeMountsFromUser(ids)
}