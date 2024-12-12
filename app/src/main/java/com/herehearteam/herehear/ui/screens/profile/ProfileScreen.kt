package com.herehearteam.herehear.ui.screens.profile

import RegisterViewModel
import android.net.Uri
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.theme.ColorPrimary
import com.herehearteam.herehear.ui.theme.HereHearTheme
import com.herehearteam.herehear.R
import com.herehearteam.herehear.navigation.Screen
import com.herehearteam.herehear.ui.components.CustomButtonFilled
import com.herehearteam.herehear.ui.components.CustomButtonOutlined
import com.herehearteam.herehear.ui.components.LocalGoogleAuthUiClient
import com.herehearteam.herehear.ui.components.ProfileOptionComponent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.ui.screens.auth.LoginViewModel
import com.herehearteam.herehear.ui.screens.auth.LoginViewModelFactory
import com.herehearteam.herehear.ui.screens.auth.RegisterViewModelFactory

@Composable
fun Container(
    photoProfile: Uri?,
    userName: String,
    isComplete: Boolean){
    Surface(
        shape = RoundedCornerShape(15.dp),
        color = ColorPrimary,
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(Dp(114.02f))

    ){
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.3f) // 30% width
                    .fillMaxHeight()
                    .padding(start = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                // Konten pertama
                Image(
                    painter = if (photoProfile != null) {
                        painterResource(id = R.drawable.avatar)
                    } else {
                        painterResource(id = R.drawable.avatar)
                    },
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(82.dp)
                        .clip(CircleShape) // Membuat gambar berbentuk lingkaran
                        .background(Color.White) // Background jika gambar tidak tersedia
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.7f) // 70% width
                    .fillMaxHeight()
            ) {
                // Konten kedua
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 18.dp)
                    .padding(vertical = 16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(0.5f) // 50% height
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        // Konten atas (Bagian pertama)
                        Column(
                            horizontalAlignment = Alignment.End // Align text to the right
                        ) {
                            Text(
                                text = "Hai, $userName!!",
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            if (isComplete) {
                                Text(
                                    text = "Edit Profilmu Di sini",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                )
                            } else {
                                Text(
                                    text = "Profil Kamu belum lengkap nih",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.5f) // 50% height
                            .fillMaxWidth()
                            .padding(start = 126.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        // Konten bawah (Bagian kedua)
                        CustomButtonFilled(
                            text = if (isComplete) "Edit" else "Lengkapi",
                            onClick = {},
                            backgroundColor = Color.White,
                            textColor = Color.Black,
                            height = Dp(33.49f)
                        )
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PopUpEmergencyContact(
//    onDismiss: () -> Unit,
//    initialContact: ProfileUiState?,
//    onShowToast: (String) -> Unit
//){
//    var name by remember { mutableStateOf(initialContact?.emergencyContact?.emergency_name ?: "") }
//    var number by remember { mutableStateOf(initialContact?.emergencyContact?.emergency_number ?: "") }
//    var relationship by remember { mutableStateOf(initialContact?.emergencyContact?.relationship ?: "") }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true
//    )
//
//
//    val viewModel = ProfileViewModel(
//        googleAuthUiClient = LocalGoogleAuthUiClient.current,
//        userPreferencesDataStore = UserPreferencesDataStore.getInstance(context = LocalContext.current),
//        emergencyContactRepository = AppDependencies.getInstance(LocalContext.current).emergencyContactRepository
//    )
//
//    val uiState by viewModel.uiState.collectAsState()
//    uiState.emergencyContact?.let { contact ->
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
//                .padding(16.dp)
//        ) {
//            Column {
//                Text("Nama: ${contact.emergency_name}", style = MaterialTheme.typography.bodyMedium)
//                Text("Nomor: ${contact.emergency_number}", style = MaterialTheme.typography.bodyMedium)
//                Text("Hubungan: ${contact.relationship}", style = MaterialTheme.typography.bodyMedium)
//            }
//        }
//    }
//
//    val currentUser = FirebaseAuth.getInstance().currentUser
//    val userId = currentUser?.uid
//
//    val view = LocalView.current
//    var isImeVisible by remember { mutableStateOf(false) }
//
//    DisposableEffect(Unit) {
//        val listener = ViewTreeObserver.OnPreDrawListener {
//            isImeVisible = ViewCompat.getRootWindowInsets(view)
//                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
//            true
//        }
//        view.viewTreeObserver.addOnPreDrawListener(listener)
//        onDispose {
//            view.viewTreeObserver.removeOnPreDrawListener(listener)
//        }
//    }
//
//    // Validasi input
//    val isFormValid = name.isNotBlank() &&
//            number.isNotBlank() &&
//            relationship.isNotBlank()
//
//    ModalBottomSheet(
//        onDismissRequest = onDismiss,
//        sheetState = sheetState,
//        windowInsets = WindowInsets.ime,
//        modifier = Modifier.then(
//            if (isImeVisible)
//                Modifier.fillMaxHeight(0.85F)
//            else
//                Modifier.fillMaxHeight(0.45F)
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            ) {
//                Text(
//                    text = "Masukan Kontak Darurat",
//                    style = TextStyle(
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 16.sp
//                    ),
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(Modifier.height(16.dp))
//
//                TextField(
//                    value = name,
//                    onValueChange = {
//                        name = it
//                        errorMessage = null
//                    },
//                    label = { Text("Nama Kontak") },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color(0xFFF5F5F5),
//                        focusedIndicatorColor = ColorPrimary,
//                        unfocusedIndicatorColor = Color.Gray
//                    ),
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    isError = name.isBlank() && errorMessage != null
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                TextField(
//                    value = number,
//                    onValueChange = {
//                        number = it
//                        errorMessage = null
//                    },
//                    label = { Text("Nomor Telepon") },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color(0xFFF5F5F5),
//                        focusedIndicatorColor = ColorPrimary,
//                        unfocusedIndicatorColor = Color.Gray
//                    ),
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                    isError = number.isBlank() && errorMessage != null
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                TextField(
//                    value = relationship,
//                    onValueChange = {
//                        relationship = it
//                        errorMessage = null
//                    },
//                    label = { Text("Hubungan") },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color(0xFFF5F5F5),
//                        focusedIndicatorColor = ColorPrimary,
//                        unfocusedIndicatorColor = Color.Gray
//                    ),
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    isError = relationship.isBlank() && errorMessage != null
//                )
//
//                // Tampilkan pesan error jika ada
//                if (errorMessage != null) {
//                    Text(
//                        text = errorMessage ?: "",
//                        color = Color.Red,
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//
//            CustomButtonFilled(
//                onClick = {
//
//                    if (isFormValid) {
//                        isLoading = true
//                        val contact = userId?.let {
//                            EmergencyContact(
//                                userId = it,
//                                emergency_name = name,
//                                emergency_number = number,
//                                relationship = relationship,
//                            )
//                        }
//                        Log.d("TAI 2", "$contact")
//                        try {
//                            if (contact != null) {
//                                viewModel.saveEmergencyContact(contact)
//                            }
//                            onShowToast("Kontak darurat berhasil disimpan.")
//                            onDismiss()
//                        } catch (e: Exception) {
//                            Log.e("EmergencyContact", "Error saving contact", e)
//                            errorMessage = "Gagal menyimpan kontak. Silakan coba lagi."
//                        } finally {
//                            isLoading = false
//                        }
//                    } else {
//                        errorMessage = "Harap isi semua field"
//                    }
//                },
//                text = if (isLoading) "Menyimpan..." else "Simpan",
//                backgroundColor = ColorPrimary,
//                modifier = Modifier.fillMaxWidth(),
//                isEnabled = isFormValid && !isLoading
//            )
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpEmergencyContact(
    onDismiss: () -> Unit,
    initialContact: ProfileUiState?,
    onShowToast: (String) -> Unit
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val viewModel = ProfileViewModel(
        googleAuthUiClient = LocalGoogleAuthUiClient.current,
        userPreferencesDataStore = UserPreferencesDataStore.getInstance(context = LocalContext.current),
        emergencyContactRepository = AppDependencies.getInstance(LocalContext.current).emergencyContactRepository
    )

    val uiState by viewModel.uiState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadEmergencyContact()
        }
    }

    var name by remember { mutableStateOf(initialContact?.emergencyContact?.emergency_name ?: uiState.emergencyContact?.emergency_name ?: "") }
    var number by remember { mutableStateOf(initialContact?.emergencyContact?.emergency_number ?: uiState.emergencyContact?.emergency_number ?: "") }
    var relationship by remember { mutableStateOf(initialContact?.emergencyContact?.relationship ?: uiState.emergencyContact?.relationship ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isEditable by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            isImeVisible = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^(\\+62|62|0)8[1-9][0-9]{6,10}\$".toRegex()
        return phoneRegex.matches(phone)
    }

    val isFormValid = name.isNotBlank() &&
            number.isNotBlank() &&
            relationship.isNotBlank() &&
            isValidPhoneNumber(number)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        windowInsets = WindowInsets.ime,
        modifier = Modifier.then(
            if (isImeVisible)
                Modifier.fillMaxHeight(0.85F)
            else
                Modifier.fillMaxHeight(0.45F)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = if (uiState.emergencyContact != null && !isEditable)
                        "Detail Kontak Darurat"
                    else "Masukan Kontak Darurat",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Nama Kontak") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = ColorPrimary,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = uiState.emergencyContact != null && !isEditable,
                    isError = name.isBlank() && errorMessage != null
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = number,
                    onValueChange = {
                        number = it
                        errorMessage = null
                    },
                    label = { Text("Nomor Telepon") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = ColorPrimary,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    readOnly = uiState.emergencyContact != null && !isEditable,
                    isError = (!isValidPhoneNumber(number) && number.isNotBlank()) || (number.isBlank() && errorMessage != null)
                )

                if (!isValidPhoneNumber(number) && number.isNotBlank()) {
                    Text(
                        text = "Nomor telepon tidak valid. Gunakan format +62 atau 08.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = relationship,
                    onValueChange = {
                        relationship = it
                        errorMessage = null
                    },
                    label = { Text("Hubungan") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = ColorPrimary,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = uiState.emergencyContact != null && !isEditable,
                    isError = relationship.isBlank() && errorMessage != null
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            CustomButtonFilled(
                onClick = {
                    if (uiState.emergencyContact != null && !isEditable) {
                        // Aktifkan mode edit
                        isEditable = true
                    } else {
                        if (isFormValid) {
                            isLoading = true
                            val contact = userId?.let {
                                EmergencyContact(
                                    userId = it,
                                    emergency_name = name,
                                    emergency_number = number,
                                    relationship = relationship,
                                )
                            }

                            Log.d("Tai", "$contact")
                            try {
                                if (contact != null) {
                                    viewModel.saveEmergencyContact(contact)
                                    onShowToast(if (uiState.emergencyContact != null) "Kontak darurat berhasil diperbarui." else "Kontak darurat berhasil disimpan.")
                                    isEditable = false
                                    onDismiss()
                                }
                            } catch (e: Exception) {
                                Log.e("EmergencyContact", "Error saving contact", e)
                                errorMessage = "Gagal menyimpan kontak. Silakan coba lagi."
                                onShowToast("Gagal menyimpan kontak. Periksa koneksi internet Anda.")
                            } finally {
                                isLoading = false
                            }
                        } else {
                            errorMessage = "Harap isi semua field dengan benar"
                        }
                    }
                },
                text = when {
                    isLoading -> "Menyimpan..."
                    uiState.emergencyContact != null && !isEditable -> "Edit"
                    uiState.emergencyContact != null && isEditable -> "Update"
                    else -> "Simpan"
                },
                backgroundColor = ColorPrimary,
                modifier = Modifier.fillMaxWidth(),
                isEnabled = isFormValid || (uiState.emergencyContact != null && !isEditable)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun popUpDeleteAccount(
    onDismiss: () -> Unit,
    onButtonDeleteClick: () -> Unit,
    viewModel: ProfileViewModel,
    navController: NavHostController
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        windowInsets = WindowInsets.ime,
        modifier = Modifier
            .fillMaxHeight(0.45F)
    ){

        CustomButtonFilled(
            onClick = {
//                viewModel.deleteAccount(userId.toString())
//                onButtonDeleteClick
                      },
            text = "Hapus Akun",
            backgroundColor = ColorPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ListOfOption(
    viewModel: ProfileViewModel,
    navController: NavHostController
){
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showEmergencyBottomSheet by remember { mutableStateOf(false) }
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .padding(top = 18.dp)
            .padding(bottom = 36.dp),
        verticalArrangement = Arrangement.Top
    ){
        Box(modifier = Modifier
            .padding(horizontal = 16.dp)){
            Text(
                text = "Informasi Akun",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }

        Spacer(Modifier.height(18.dp))

        ProfileOptionComponent(
            icon = Icons.Outlined.Person,
            title = "Kontak Darurat",
            onClick = {
                showEmergencyBottomSheet = true
            }
        )

        if (showEmergencyBottomSheet) {
            PopUpEmergencyContact(
                onDismiss = {
                    showEmergencyBottomSheet = false
                },
                initialContact = uiState,
                onShowToast = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(Modifier.height(10.dp))

        ProfileOptionComponent(
            icon = Icons.Outlined.MailOutline,
            title = "Kirim Saran",
            onClick = { }
        )

        Spacer(Modifier.height(10.dp))

        ProfileOptionComponent(
            icon = Icons.Outlined.Star,
            title = "Kirim Ulasan",
            onClick = { }
        )

        Spacer(Modifier.height(10.dp))

        ProfileOptionComponent(
            icon = Icons.Outlined.Delete,
            title = "Hapus Akun",
            onClick = {
                showDeleteBottomSheet = true
            }
        )
        if (showDeleteBottomSheet) {
            popUpDeleteAccount(
                onDismiss = {
                    showDeleteBottomSheet = false
                },
                viewModel = viewModel,
                navController = navController,
                onButtonDeleteClick = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            LocalGoogleAuthUiClient.current,
            userPreferencesDataStore = UserPreferencesDataStore.getInstance(context = LocalContext.current),
            context = LocalContext.current
        )
    )
) {
    val apiService = ApiConfig.getApiService()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            apiService,
            AppDependencies.getInstance(LocalContext.current).userRepository
        )
    )
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            AppDependencies.getInstance(LocalContext.current).userRepository,
            AppDependencies.getInstance(LocalContext.current).googleAuthUiClient
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadEmergencyContact()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CustomTopAppBar(
                    pageTitle = "Profil",
                    icon = Icons.Filled.ArrowBackIosNew
                )
            }
        },
    ) { innerPadding ->
        // Konten Halaman Profil
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // First content: Container
            Container(
                photoProfile = uiState.photoUrl?.let { Uri.parse(it) },
                userName = uiState.userName ?: "User",
                isComplete = false
            )

            // Second content: ListOfOption
            ListOfOption(
                viewModel,
                navController)

            // Third content: LogoutButton
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ){
                CustomButtonOutlined(
                    onClick = {
                        scope.launch {
                            viewModel.signOut()
                            registerViewModel.resetRegistrationState()
                            loginViewModel.resetLoginState()
                            Log.d("ProfileViewModel", "Sign out called - Register state reset")
                            Log.d("ProfileViewModel", "Sign out called - Login state reset")
                            Toast.makeText(
                                context,
                                "Berhasil logout",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    text = "Logout",
                    icon = painterResource(R.drawable.ic_logout),
                    textColor = MaterialTheme.colorScheme.onBackground,
                    iconColor = Color.Red,
                    outlineColor = Color.Red
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileScreenPreview(){
    val navController = rememberNavController()
    HereHearTheme {
        ProfileScreen(navController = navController)
    }
}