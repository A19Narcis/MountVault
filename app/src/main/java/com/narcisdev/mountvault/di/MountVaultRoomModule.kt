package com.narcisdev.mountvault.di

import android.content.Context
import androidx.room.Room
import com.narcisdev.mountvault.data.dao.ExpansionDao
import com.narcisdev.mountvault.data.dao.MountDao
import com.narcisdev.mountvault.data.database.MountVaultDatabase
import com.narcisdev.mountvault.domain.repository.AvatarRepository
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import com.narcisdev.mountvault.domain.repository.MountRepository
import com.narcisdev.mountvault.domain.usecase.StartMountsSyncUseCase
import com.narcisdev.mountvault.domain.usecase.SyncAvatarsUseCase
import com.narcisdev.mountvault.domain.usecase.SyncExpansionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MountVaultRoomModule {
    const val MOUNT_VAULT_DATABASE_NAME = "mount_vault_database_v3"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context, MountVaultDatabase::class.java, MOUNT_VAULT_DATABASE_NAME).build()

    @Provides
    fun provideMountDao(db: MountVaultDatabase): MountDao = db.mountDao()

    @Provides
    @Singleton
    fun provideMountUseCase(repo: MountRepository)= StartMountsSyncUseCase(repo)

    @Provides
    @Singleton
    fun providesExpansionDao(db: MountVaultDatabase): ExpansionDao = db.expansionDao()
    @Provides
    @Singleton
    fun provideExpansionUseCase(repo: ExpansionRepository) = SyncExpansionsUseCase(repo)

    @Provides
    @Singleton
    fun provideAvatarDao(db: MountVaultDatabase) = db.avatarsDao()

    @Provides
    @Singleton
    fun provideAvatarUseCase(repo: AvatarRepository) = SyncAvatarsUseCase(repo)



}