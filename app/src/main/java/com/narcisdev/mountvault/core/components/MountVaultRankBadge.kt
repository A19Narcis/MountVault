package com.narcisdev.mountvault.core.components

import android.R.attr.maxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.core.theme.WowFont

@Composable
fun MountVaultRankBadge(
    rank: String,
    isSelected: Boolean = true,
    modifier: Modifier = Modifier
) {
    val (activeBgColor, activeBorderColor) = when (rank) {
        "legendary" -> Color(0xFFFFE47A) to Color(0xFFB8860B)
        "epic" -> Color(0xFFD8B0FF) to Color(0xFF7A1FA2)
        "rare" -> Color(0xFFA8CFFF) to Color(0xFF0A4CA3)
        "common" -> Color(0xFFD9D9D9) to Color(0xFF6B6B6B)
        else -> Color(0xFFCCCCCC) to Color(0xFF444444)
    }

    val bgColor = if (isSelected) activeBgColor else Color(0xFFE0E0E0)
    val borderColor = if (isSelected) activeBorderColor else Color(0xFF9E9E9E)
    val textColor = borderColor.copy(alpha = if (isSelected) 1f else 0.6f)

    val textRank = when(rank.lowercase()) {
        "legendary" -> "S"
        "epic" -> "A"
        "rare" -> "B"
        "common" -> "C"
        else -> "S"
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textRank,
            fontFamily = WowFont,
            color = textColor,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}