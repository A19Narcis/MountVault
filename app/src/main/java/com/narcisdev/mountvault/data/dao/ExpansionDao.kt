package com.narcisdev.mountvault.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity

@Dao
interface ExpansionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mounts: List<ExpansionEntity>)

    @Query("SELECT * FROM expansions")
    suspend fun getAllExpansions(): List<ExpansionEntity>
}