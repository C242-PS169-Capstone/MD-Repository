package com.herehearteam.herehear.ui.screens.auth

import RegisterViewModel
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
fun RegisterScreen(
    onRegisterWithGmail: () -> Unit,
    onNavigateBack: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            AppDependencies.getInstance(LocalContext.current).userRepository
        )
    ),
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.resetRegistrationState()
    }

    fun validateName(): Boolean {
        nameError = when {
            name.isBlank() -> "Name cannot be empty"
            name.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }
        return nameError == null
    }

    fun validateEmail(): Boolean {
        emailError = when {
            email.isBlank() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
        return emailError == null
    }

    fun validatePassword(): Boolean {
        passwordError = when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
        return passwordError == null
    }

    fun validateConfirmPassword(): Boolean {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
        return confirmPasswordError == null
    }

    LaunchedEffect(key1 = state.registrationError) {
        state.registrationError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            onRegisterSuccess()
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Bergabung dengan HereHear",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Mulai tulis dan temukan rekomendasi terbaik untuk mentalmu.",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            CustomTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = "Name",
                icon = Icons.Default.Person,
                errorText = nameError,
                imeAction = ImeAction.Next
            )

            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = "Email",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                errorText = emailError,
                imeAction = ImeAction.Next
            )

            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = "Password",
                icon = Icons.Default.Lock,
                isPasswordField = true,
                keyboardType = KeyboardType.Password,
                errorText = passwordError,
                imeAction = ImeAction.Next
            )

            CustomTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = "Confirm Password",
                icon = Icons.Default.Lock,
                isPasswordField = true,
                keyboardType = KeyboardType.Password,
                errorText = confirmPasswordError,
                imeAction = ImeAction.Done
            )

            CustomButtonFilled(
                text = "Register",
                onClick = {
                    val isNameValid = validateName()
                    val isEmailValid = validateEmail()
                    val isPasswordValid = validatePassword()
                    val isConfirmPasswordValid = validateConfirmPassword()

                    if (isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
                        viewModel.registerWithEmailPassword(name, email, password)
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = "atau",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }

            CustomButtonFilled(
                text = "Register with Google",
                backgroundColor = Color.White,
                textColor = Color.Black,
                fontSize = 14.sp,
                icon = painterResource(R.drawable.ic_logo_google),
                onClick = onRegisterWithGmail
            )

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.Gray
                )
                Text(
                    text = "Login",
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onLoginClick()
                    }
                )
            }
        }
    }
    if (state.isLoading) {
        RegisterDialog()
    }
}

@Composable
fun RegisterDialog() {
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
                    text = "Sedang Daftar...",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterPreview(){
    RegisterScreen(
        onRegisterWithGmail = { },
        onNavigateBack = { },
        onLoginClick = { },
        onRegisterSuccess = { }
    )
}