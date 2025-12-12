package com.narcisdev.mountvault.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MountVaultErrorText(
    error: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = error,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontStyle = FontStyle.Italic,
        color = Color.Red,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    )
}