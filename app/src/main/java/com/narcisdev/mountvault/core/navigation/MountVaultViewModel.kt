package com.narcisdev.mountvault.core.navigation

import androidx.lifecycle.ViewModel
import com.narcisdev.mountvault.domain.usecase.StartMountsSyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MountVaultViewModel @Inject constructor(
    startMountsSyncUseCase: StartMountsSyncUseCase
) : ViewModel() {

    init {
        startMountsSyncUseCase()
    }
}