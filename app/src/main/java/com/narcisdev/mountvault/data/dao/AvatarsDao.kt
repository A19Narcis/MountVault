package com.narcisdev.mountvault.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.MountEntity

@Dao
interface AvatarsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(avatars: List<AvatarEntity>)

    @Query("SELECT * FROM avatars")
    suspend fun getAllAvatars(): List<AvatarEntity>
}