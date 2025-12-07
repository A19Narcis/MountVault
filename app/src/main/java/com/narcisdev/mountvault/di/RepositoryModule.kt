package com.narcisdev.mountvault.di

import com.narcisdev.mountvault.data.repository.AuthRepositoryImpl
import com.narcisdev.mountvault.data.repository.UserRepositoryImpl
import com.narcisdev.mountvault.domain.repository.AuthRepository
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
}
