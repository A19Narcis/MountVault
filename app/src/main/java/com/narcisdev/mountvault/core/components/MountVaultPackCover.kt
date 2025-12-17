package com.narcisdev.mountvault.core.components

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.core.theme.wow_darkBlue
import com.narcisdev.mountvault.core.theme.wow_darkGolden
import com.narcisdev.mountvault.core.theme.wow_golden
import com.narcisdev.mountvault.core.theme.wow_lightBlue

@Composable
fun MountVaultPackCover(
    scale: Float = 1f,
    packName: String = "Mythic",
) {

    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .aspectRatio(0.7f)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val crossThickness = w * 0.15f  // grosor de la cruz
            val innerThickness = crossThickness * 0.5f // doble raya

            // Dibujar las 4 esquinas oscuras
            drawRect(
                color = wow_darkGolden,
                topLeft = Offset(0f, 0f),
                size = Size(w/2 - crossThickness/2, h/2 - crossThickness/2)
            )
            drawRect(
                color = wow_darkGolden,
                topLeft = Offset(w/2 + crossThickness/2, 0f),
                size = Size(w/2 - crossThickness/2, h/2 - crossThickness/2)
            )
            drawRect(
                color = wow_darkGolden,
                topLeft = Offset(0f, h/2 + crossThickness/2),
                size = Size(w/2 - crossThickness/2, h/2 - crossThickness/2)
            )
            drawRect(
                color = wow_darkGolden,
                topLeft = Offset(w/2 + crossThickness/2, h/2 + crossThickness/2),
                size = Size(w/2 - crossThickness/2, h/2 - crossThickness/2)
            )

            // Dibujar la cruz (doble raya)
            val centerX = w/2
            val centerY = h/2

            // Raya horizontal exterior
            drawRect(
                color = wow_lightBlue,
                topLeft = Offset(0f, centerY - crossThickness/2),
                size = Size(w, crossThickness)
            )
            // Raya horizontal interior
            drawRect(
                color = wow_darkBlue,
                topLeft = Offset(0f, centerY - innerThickness/2),
                size = Size(w, innerThickness)
            )

            // Raya vertical exterior
            drawRect(
                color = wow_lightBlue,
                topLeft = Offset(centerX - crossThickness/2, 0f),
                size = Size(crossThickness, h)
            )
            // Raya vertical interior
            drawRect(
                color = wow_darkBlue,
                topLeft = Offset(centerX - innerThickness/2, 0f),
                size = Size(innerThickness, h)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.main_warcraft_icon),
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp)
            )
        }

        // Texto del pack
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                fontFamily = WowFont,
                text = "$packName Pack",
                color = wow_golden,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}