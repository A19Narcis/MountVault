package com.narcisdev.mountvault

//import com.narcisdev.mountvault.core.navigation.MountVaultApp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.core.navigation.MountVaultAppNav3
import com.narcisdev.mountvault.core.navigation.MountVaultViewModel
import com.narcisdev.mountvault.core.theme.MountVaultTheme
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        enableEdgeToEdge()
        setContent {
            val appViewModel: MountVaultViewModel = hiltViewModel()
            userPreferencesDataSource = UserPreferencesDataSource(this)
            MountVaultTheme {
                MountVaultAppNav3(userPreferencesDataSource = userPreferencesDataSource)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            Log.i(Constants.APP_NAME, currentUser.email.toString())
        }
    }
}