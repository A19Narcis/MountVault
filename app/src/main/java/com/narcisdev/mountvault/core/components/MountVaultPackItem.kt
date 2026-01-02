package com.narcisdev.mountvault.core.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.narcisdev.mountvault.core.theme.wow_golden
import com.narcisdev.mountvault.domain.entity.PackEntity
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun MountVaultPackItem(
    pack: PackEntity,
    scale: Float,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isRestrictedPack = pack.name == "Epic" || pack.name == "Mythic"
    val isAvailable = !isRestrictedPack || isEpicMythicAvailable()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .aspectRatio(0.7f)
            .border(
                width = 6.dp,
                color = wow_golden,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (isAvailable) {
                    onClick()
                } else {
                    Toast
                        .makeText(
                            context,
                            "Available between friday (17h CET) and sunday (23h CET)",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            MountVaultPackCover(packName = pack.name, isAvailable = isAvailable)
        }
    }
}

fun isEpicMythicAvailable(): Boolean {
    val now = ZonedDateTime.now(ZoneId.of("Europe/Madrid"))

    val day = now.dayOfWeek
    val hour = now.hour

    return when (day) {
        DayOfWeek.FRIDAY -> hour >= 17
        DayOfWeek.SATURDAY -> true
        DayOfWeek.SUNDAY -> hour < 23
        else -> false
    }
}
