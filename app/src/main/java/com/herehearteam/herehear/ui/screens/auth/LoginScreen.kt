package com.herehearteam.herehear.ui.screens.auth

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.R
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomTextField
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.theme.ColorPrimary

@Composable
fun LoginScreen(
    onLoginWithGmail: () -> Unit,
    onNavigateBack: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            AppDependencies.getInstance(LocalContext.current).userRepository,
            AppDependencies.getInstance(LocalContext.current).googleAuthUiClient
        )
    )
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Log.d("LoginScreen", "Login status: ${state.isSignInSuccessful}")
        viewModel.resetLoginState()
        Log.d("LoginScreen", "Login status afer : ${state.isSignInSuccessful}")

    }

    fun validateEmail(): Boolean {
        emailError = when {
            email.isBlank() -> "Email tidak boleh kosong"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid"
            else -> null
        }
        return emailError == null
    }

    fun validatePassword(): Boolean {
        passwordError = when {
            password.isBlank() -> "Password tidak boleh kosong"
            password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }
        return passwordError == null
    }

    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            CustomTopAppBar(
                pageTitle = "Register",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onIconClick = onNavigateBack
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_herehear),
                contentDescription = "HereHear Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 16.dp)
            )

            // Email TextField
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    loginErrorMessage = null
                },
                label = "Email",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                errorText = emailError,
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    loginErrorMessage = null
                },
                label = "Password",
                icon = Icons.Default.Lock,
                isPasswordField = true,
                keyboardType = KeyboardType.Password,
                errorText = passwordError,
                imeAction = ImeAction.Done
            )

            // Error Message Display
            loginErrorMessage?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            CustomButtonFilled(
                text = "Login",
                onClick = {
                    loginErrorMessage = null
                    emailError = null
                    passwordError = null

                    val isEmailValid = validateEmail()
                    val isPasswordValid = validatePassword()

                    if (isEmailValid && isPasswordValid) {
                        viewModel.loginWithEmailPassword(email, password)
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Black
                )
                Text(
                    text = "atau",
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            CustomButtonFilled(
                text = "Masuk Dengan Google",
                backgroundColor = Color.White,
                textColor = Color.Black,
                icon = painterResource(R.drawable.ic_logo_google),
                onClick = onLoginWithGmail
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun? ",
                    color = Color.Gray
                )
                Text(
                    text = "Daftar",
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }

    }

    if (state.isLoading) {
        LoadingDialog()
    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = ColorPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sedang Masuk...",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen(
        onLoginWithGmail = { },
        onNavigateBack = { },
        onRegisterClick = { },
    )
}