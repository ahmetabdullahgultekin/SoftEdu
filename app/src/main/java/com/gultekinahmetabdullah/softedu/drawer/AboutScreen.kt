package com.gultekinahmetabdullah.softedu.drawer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AboutScreen() {

    val aboutText = "SoftEdu is a mobile application that aims to improve software and " +
            "engineering knowledge of people from all ages. " +
            "SoftEdu has smooth, brief, simple and understandable user interface design " +
            "in order to satisfy more comprehensive and accessible display for " +
            "children or older people. Furthermore; since SoftEdu appeals for " +
            "every person wants to learn or dive deeper in programming " +
            "(or in other words software design - abstract engineering), everyone will be " +
            "able to use SoftEdu alike whatever his or her age. Additionally, we know that " +
            "enjoying while learning is how much crucial so, we can emphasize that our design " +
            "pleases client's pleasure.Therefore we can say that SoftEdu fulfills the " +
            "entertaining teaching methods like games, quizzes, puzzles and so on. " +
            "Eventually, this is a flexible platform that presents unique experience by " +
            "requesting the user knowledge level. Finally as a student of SoftEdu, you will " +
            "admire your flawless software knowledge!"

    AboutCard(aboutText)

    BallRollingInEllipse()

}

@Composable
fun AboutCard(aboutText: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Card(
            modifier = Modifier
                .alpha(0.75f)
                .align(Alignment.CenterHorizontally)//TODO Vertical center
                .padding(16.dp), shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = aboutText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BallRollingInEllipse() {
    val strokeWidth = 5f
    val ballRadiusDP = 7.5.dp
    val ellipseWidthDP = 150.dp
    val ellipseHeightDP = 600.dp

    val ball1Position = remember { Animatable(0f) }
    val ball2Position = remember { Animatable(0f) }
    val ball3Position = remember { Animatable(0f) }
    val ball4Position = remember { Animatable(0f) }
    val ballRadius = listOf(ballRadiusDP, ballRadiusDP, ballRadiusDP, ballRadiusDP)
    val ellipseWidth = listOf(ellipseWidthDP, ellipseWidthDP, ellipseWidthDP, ellipseWidthDP)
    val ellipseHeight = listOf(ellipseHeightDP, ellipseHeightDP, ellipseHeightDP, ellipseHeightDP)


    LaunchedEffect(key1 = true) {
        ball1Position.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    LaunchedEffect(key1 = true) {
        ball2Position.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    LaunchedEffect(key1 = true) {
        ball3Position.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    LaunchedEffect(key1 = true) {
        ball4Position.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        //.size(ellipseWidth + ballRadius * 2, ellipseHeight + ballRadius * 2)

        val center = Offset(size.width / 2, size.height / 2)
        val half1Width = ellipseWidth[0].toPx() / 2
        val half1Height = ellipseHeight[0].toPx() / 2
        val half2Width = ellipseWidth[1].toPx() / 2
        val half2Height = ellipseHeight[1].toPx() / 2
        val half3Width = ellipseWidth[2].toPx() / 2
        val half3Height = ellipseHeight[2].toPx() / 2
        val half4Width = ellipseWidth[3].toPx() / 2
        val half4Height = ellipseHeight[3].toPx() / 2

        rotate(45f, center) {
            drawOval(
                color = Color.LightGray,
                topLeft = Offset(center.x - half1Width / 2, center.y - half1Height / 2),
                size = Size(half1Width, half1Height),
                style = Stroke(strokeWidth)
            )
        }

        rotate(-45f, center) {
            drawOval(
                color = Color.LightGray,
                topLeft = Offset(center.x - half1Width / 2, center.y - half1Height / 2),
                size = Size(half1Width, half1Height),
                style = Stroke(strokeWidth)
            )
        }

        drawOval(
            color = Color.LightGray,
            topLeft = Offset(center.x - half3Width / 2, center.y - half3Height / 2),
            size = Size(half3Width, half3Height),
            style = Stroke(strokeWidth)
        )

        rotate(90f, center) {
            drawOval(
                color = Color.LightGray,
                topLeft = Offset(center.x - half1Width / 2, center.y - half1Height / 2),
                size = Size(half1Width, half1Height),
                style = Stroke(strokeWidth)
            )
        }

        val offset = 2 * PI / 8 // 45 degrees in radians

        val angle1 = ball1Position.value * 2 * PI
        val ballX1 = center.x + half1Width / 2 * cos(angle1).toFloat()
        val ballY1 = center.y + half1Height / 2 * sin(angle1).toFloat()

        val angle2 = ball2Position.value * 2 * PI
        val ballX2 = center.x + half2Width / 2 * cos(angle2).toFloat()
        val ballY2 = center.y + half2Height / 2 * sin(angle2).toFloat()

        val angle3 = ball3Position.value * 2 * PI
        val ballX3 = center.x + half3Width / 2 * cos(angle3).toFloat()
        val ballY3 = center.y + half3Height / 2 * sin(angle3).toFloat()

        val angle4 = ball4Position.value * 2 * PI
        val ballX4 = center.x + half4Width / 2 * cos(angle4).toFloat()
        val ballY4 = center.y + half4Height / 2 * sin(angle4).toFloat()

        rotate(45f, center) {
            drawCircle(
                color = Color.Red,
                center = Offset(ballX1, ballY1),
                radius = ballRadius[0].toPx()
            )
        }
        rotate(-45f, center) {
            drawCircle(
                color = Color.Blue,
                center = Offset(ballX2, ballY2),
                radius = ballRadius[1].toPx()
            )
        }
        rotate(90f, center) {
            drawCircle(
                color = Color.Green,
                center = Offset(ballX3, ballY3),
                radius = ballRadius[2].toPx()
            )
        }

        drawCircle(
            color = Color.Yellow,
            center = Offset(ballX4, ballY4),
            radius = ballRadius[3].toPx()
        )
    }
}