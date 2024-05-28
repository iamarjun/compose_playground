package com.arjun.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arjun.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ScoreGenerator()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        Greeting("Android")
    }
}

@Composable
fun ArcProgressbar(
    modifier: Modifier = Modifier,
    newScore: Float,
    startAngle: Float = -180f,
    limitAngle: Float = 180f,
    thickness: Dp = 25.dp
) {

    val animateValue = remember { Animatable(0f) }

    LaunchedEffect(newScore) {
        if (newScore > 0f) {
            animateValue.snapTo(0f)
            delay(10)
            animateValue.animateTo(
                targetValue = 0.6f * limitAngle,
                animationSpec = tween(
                    durationMillis = 1000
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
                .align(Alignment.Center)
                .background(color = Color.Yellow),
            onDraw = {
                // Background Arc
                drawArc(
                    color = Color(0xFFDAD1D1).copy(alpha = 0.25f),
                    startAngle = startAngle,
                    sweepAngle = limitAngle,
                    useCenter = false,
                    style = Stroke(thickness.toPx(), cap = StrokeCap.Square),
                    size = Size(2f * size.width, 2f * size.height),
                    topLeft = Offset(-0.5f * size.width, 0.dp.toPx()),
                )

                // Foreground Arc
                drawArc(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF3FEFD),
                            Color(0xFF00C197).copy(alpha = 0.25f),
                            Color(0xFF00C197)
                        )
                    ),
                    startAngle = startAngle,
                    sweepAngle = animateValue.value,
                    useCenter = false,
                    style = Stroke(thickness.toPx(), cap = StrokeCap.Round),
                    size = Size(2f * size.width, 2f * size.height),
                    topLeft = Offset(-0.5f * size.width, 0.dp.toPx()),
                )
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            text = "₹50",
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 86.sp,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFF00C197),
                shadow = Shadow(
                    color = Color.LightGray,
                    offset = Offset(0f, 7f),
                    blurRadius = 36f
                )
            )
        )
    }
}

@Composable
fun ScoreGenerator() {

    var newScore by remember {
        mutableFloatStateOf(0f)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            newScore += 30f
        }) {
            Text("Add Score + 30")
        }

        Spacer(modifier = Modifier.height(20.dp))

        ArcProgressbar(newScore = newScore)
    }
}

val fontFamily = FontFamily(
    Font(R.font.basiercircle_regular, FontWeight.Normal),
    Font(R.font.basiercircle_medium, FontWeight.Medium),
    Font(R.font.basiercircle_semibold, FontWeight.SemiBold),
    Font(R.font.basiercircle_bold, FontWeight.Bold)
)