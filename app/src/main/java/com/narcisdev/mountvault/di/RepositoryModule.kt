package com.narcisdev.mountvault.di

import com.narcisdev.mountvault.data.repository.AuthRepositoryImpl
import com.narcisdev.mountvault.data.repository.AvatarRepositoryImpl
import com.narcisdev.mountvault.data.repository.ExpansionRepositoryImpl
import com.narcisdev.mountvault.data.repository.MountRepositoryImpl
import com.narcisdev.mountvault.data.repository.UserRepositoryImpl
import com.narcisdev.mountvault.domain.repository.AuthRepository
import com.narcisdev.mountvault.domain.repository.AvatarRepository
import com.narcisdev.mountvault.domain.repository.ExpansionRepository
import com.narcisdev.mountvault.domain.repository.MountRepository
import com.narcisdev.mountvault.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl:AuthRepositoryImpl):AuthRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindMountRepository(impl: MountRepositoryImpl): MountRepository

    @Singleton
    @Binds
    abstract fun bindExpansionRepository(impl: ExpansionRepositoryImpl): ExpansionRepository

    @Singleton
    @Binds
    abstract fun bindAvatarRepository(impl: AvatarRepositoryImpl): AvatarRepository
}
