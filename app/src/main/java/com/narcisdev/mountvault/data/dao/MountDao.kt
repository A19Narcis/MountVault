package com.narcisdev.mountvault.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narcisdev.mountvault.domain.entity.MountEntity

@Dao
interface MountDao {

    //Insert mounts in Room
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mounts: List<MountEntity>)

    // Get all mounts
    @Query("SELECT * FROM mounts")
    suspend fun getAllMounts(): List<MountEntity>

    // Get mounts from user
    @Query("SELECT * FROM mounts WHERE id IN (:ids)")
    suspend fun getAllMountsFromUser(ids: List<String>): List<MountEntity>
}