package com.narcisdev.mountvault.core.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MountVaultCurvedText(
    text: String,
    radius: Float = 150f,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size((radius * 2).dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val totalAngle = 180f // grados del arco
        val angleStep = totalAngle / (text.length - 1)

        for ((i, char) in text.withIndex()) {
            val angle = Math.toRadians((i * angleStep - totalAngle / 2).toDouble())
            val x = centerX + radius * cos(angle).toFloat()
            val y = centerY - radius * sin(angle).toFloat()

            drawContext.canvas.nativeCanvas.apply {
                save()
                translate(x, y)
                rotate(-90f + i * angleStep) // opcional: rotar cada letra para seguir el arco
                drawText(
                    char.toString(),
                    0f,
                    0f,
                    Paint().apply {
                        color = Color.BLACK
                        textSize = 50f
                        isAntiAlias = true
                        textAlign = Paint.Align.CENTER
                    }
                )
                restore()
            }
        }
    }
}