package com.narcisdev.mountvault.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.narcisdev.mountvault.domain.entity.MountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mounts: List<MountEntity>)

    @Query("DELETE FROM mounts")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(mounts: List<MountEntity>) {
        clearAll()
        insertAll(mounts)
    }

    @Query("SELECT * FROM mounts")
    fun observeAllMounts(): Flow<List<MountEntity>>

    @Query("SELECT * FROM mounts WHERE id IN (:ids)")
    fun observeMountsFromUser(ids: List<String>): Flow<List<MountEntity>>
}