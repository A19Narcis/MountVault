package com.narcisdev.mountvault.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.narcisdev.mountvault.data.dao.AvatarsDao
import com.narcisdev.mountvault.data.dao.ExpansionDao
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.entity.AvatarEntity

@Database(entities = [MountEntity::class, ExpansionEntity::class, AvatarEntity::class], version = 8)
@TypeConverters(Converters::class)
abstract class MountVaultDatabase : RoomDatabase() {
    abstract fun mountDao(): MountDao
    abstract fun expansionDao(): ExpansionDao
    abstract fun avatarsDao(): AvatarsDao

    companion object {
        @Volatile private var INSTANCE: MountVaultDatabase? = null

        fun getDatabase(context: Context): MountVaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = MountVaultDatabase::class.java,
                    name = "mount_vault_database"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}