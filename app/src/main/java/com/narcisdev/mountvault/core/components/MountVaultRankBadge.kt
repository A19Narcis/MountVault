package com.narcisdev.mountvault.core.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.core.theme.WowFont

@Composable
fun MountVaultRankBadge(
    rank: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, borderColor) = when (rank) {
        "legendary" -> Color(0xFFFFE47A) to Color(0xFFB8860B)
        "epic" -> Color(0xFFD8B0FF) to Color(0xFF7A1FA2)
        "rare" -> Color(0xFFA8CFFF) to Color(0xFF0A4CA3)
        "common" -> Color(0xFFD9D9D9) to Color(0xFF6B6B6B)
        else -> Color(0xFFCCCCCC) to Color(0xFF444444)
    }

    val textRank = when(rank.lowercase()) {
        "legendary" -> "S"
        "epic" -> "A"
        "rare" -> "B"
        "common" -> "C"
        else -> "S"
    }

    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textRank,
            fontFamily = WowFont,
            textAlign = TextAlign.Center,
            color = borderColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}