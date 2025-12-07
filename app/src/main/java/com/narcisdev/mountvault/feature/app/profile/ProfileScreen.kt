package com.narcisdev.mountvault.feature.app.profile

import android.content.res.Configuration
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.navigation.Routes
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar

@Composable
fun ProfileScreen(
    padding: PaddingValues, goToSettings: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.align(Alignment.End).clickable { goToSettings() },
            painter = painterResource(R.drawable.toniki), contentDescription = null
        )
        Spacer(Modifier.weight(1f))
        Text(text = "PANTALLA PROFILE")
        Spacer(Modifier.weight(1f))
    }
}

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
fun PreviewLoginLight() {
    val backStack = remember { mutableStateListOf<Routes>() }
    Scaffold(bottomBar = { MyNavigationBar(currentRoute = Routes.Profile, backStack) }) { padding ->
        ProfileScreen(padding) {}
    }
}