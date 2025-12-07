package com.narcisdev.mountvault.di

import com.narcisdev.mountvault.domain.repository.AuthRepository
import com.narcisdev.mountvault.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoginUseCase(repo:AuthRepository)= LoginUseCase(repo)
}
