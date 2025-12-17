package com.narcisdev.mountvault.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.narcisdev.mountvault.data.dao.AvatarsDao
import com.narcisdev.mountvault.data.dao.ExpansionDao
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity

@Database(
    entities = [MountEntity::class, ExpansionEntity::class, AvatarEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class MountVaultDatabase : RoomDatabase() {

    abstract fun mountDao(): MountDao
    abstract fun expansionDao(): ExpansionDao
    abstract fun avatarsDao(): AvatarsDao

    companion object {
        @Volatile
        private var INSTANCE: MountVaultDatabase? = null

        private val MIGRATION_8_1 = object : Migration(8, 1) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Borrar tablas antiguas
                db.execSQL("DROP TABLE IF EXISTS mounts")
                db.execSQL("DROP TABLE IF EXISTS expansions")
                db.execSQL("DROP TABLE IF EXISTS avatars")

                // Crear tablas según tu versión 1
                db.execSQL("""
                    CREATE TABLE mounts (
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        faction TEXT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        rarity TEXT NOT NULL,
                        cost TEXT NOT NULL,
                        dropRate REAL NOT NULL,
                        expansionId TEXT NOT NULL,
                        source TEXT NOT NULL,
                        type TEXT NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE expansions (
                        id TEXT PRIMARY KEY NOT NULL,
                        name TEXT NOT NULL,
                        coverUrl TEXT NOT NULL,
                        mounts TEXT NOT NULL,
                        year TEXT NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE avatars (
                        url TEXT PRIMARY KEY NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): MountVaultDatabase {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MountVaultDatabase::class.java,
                    "mount_vault_database_v2"
                )
                    //.addMigrations(MIGRATION_8_1)
                    .fallbackToDestructiveMigration(true)
                    .fallbackToDestructiveMigrationOnDowngrade(true)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}