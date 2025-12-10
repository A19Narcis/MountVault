package com.narcisdev.mountvault.core.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.narcisdev.mountvault.core.theme.ExpansionColors

@Composable
fun ExpansionMountsProgressBar(
    progress: Float,
    colors: List<Color>
) {
    val animatedProgress by animateFloatAsState(progress)
    val gradient = Brush.horizontalGradient(colors)

    Box(
        modifier = Modifier
            .padding(end = 20.dp)
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(brush = gradient)
        )
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
    Scaffold() { padding ->
        Column (modifier = Modifier.padding(padding)) {
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Vanilla)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.BurningCrusade)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Wrath)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Cataclysm)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Pandaria)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Draenor)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Legion)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.BFA)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Shadowlands)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.Dragonflight)
            Spacer(Modifier.height(10.dp))
            ExpansionMountsProgressBar(progress = 0.5f, colors = ExpansionColors.TWW)
            Spacer(Modifier.height(10.dp))

        }
    }

}