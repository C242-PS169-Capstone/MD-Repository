package com.herehearteam.herehear.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.R
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.theme.ColorPrimary
import kotlinx.coroutines.delay

private val MochiyPopOne = FontFamily(
    Font(R.font.mochiy_pop_one_reguler)
)

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToWelcome: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel = viewModel(
        factory = SplashViewModelFactory(
            AppDependencies.getInstance(LocalContext.current).userRepository
        )
    )
) {
    var isRotating by remember { mutableStateOf(false) }
    var showWhiteCircle by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    val rotation = animateFloatAsState(
        targetValue = if (isRotating) 360f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        finishedListener = {
            showWhiteCircle = true
            showText = true
        }
    )

    val whiteCircleScale = animateFloatAsState(
        targetValue = if (showWhiteCircle) 15f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val logoScale = animateFloatAsState(
        targetValue = if (showWhiteCircle) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val textAlpha = animateFloatAsState(
        targetValue = if (showText) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    val loginState by viewModel.uiState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is SplashUiState.Authenticated -> {
                navigateToHome()
            }
            is SplashUiState.Unauthenticated -> {
                delay(100)
                isRotating = true
                delay(1000)
                showWhiteCircle = true
                delay(500)
                showText = true
                delay(1000)
                navigateToWelcome()
            }
            SplashUiState.Loading -> {
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorPrimary),
        contentAlignment = Alignment.Center
    ) {
        if (showWhiteCircle) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(whiteCircleScale.value)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = rememberVectorPainter(
                    image = ImageVector.vectorResource(id = R.drawable.ic_herehear)
                ),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .rotate(rotation.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (!showWhiteCircle) Color.White else ColorPrimary,
                            fontFamily = MochiyPopOne,
                            fontSize = 24.sp
                        )
                    ) {
                        append("HereHear")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = if (!showWhiteCircle) Color.White else Color(0xFF57D7FE),
                            fontFamily = MochiyPopOne,
                            fontSize = 24.sp
                        )
                    ) {
                        append(".")
                    }
                },
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}