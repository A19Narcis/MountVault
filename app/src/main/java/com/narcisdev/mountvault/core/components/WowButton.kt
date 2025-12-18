package com.narcisdev.mountvault.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.core.theme.wow_darkBlue
import com.narcisdev.mountvault.core.theme.wow_darkGolden
import com.narcisdev.mountvault.core.theme.wow_golden
import com.narcisdev.mountvault.core.theme.wow_lightBlue

@Composable
fun WowButton(
    text: String,
    fontSize: Int = 18,
    letterSpacing: Int = 1,
    fontWeight: FontWeight = FontWeight.Bold,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(14.dp),
                clip = false
            )
            .border(
                width = 3.dp,
                brush = Brush.horizontalGradient(listOf(wow_golden, wow_darkGolden)),
                shape = RoundedCornerShape(14.dp)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(wow_lightBlue, wow_darkBlue)
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ){ onClick() }
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = text,
            color = wow_golden,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            letterSpacing = letterSpacing.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}