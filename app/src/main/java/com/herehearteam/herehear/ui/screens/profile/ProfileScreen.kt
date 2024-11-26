package com.herehearteam.herehear.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
                                    fontSize = 14.sp
                                )
                            )
                            if (isComplete) {
                                Text(
                                    text = "Edit Profilmu Di sini",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 14.sp
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
                            height = Dp(33.49f),
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUp(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            title?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            content?.invoke(this)
        }
    }
}

@Composable
fun ListOfOption(){
    Column(
        modifier = Modifier
            .padding(top = 18.dp)
            .padding(bottom = 36.dp),
        verticalArrangement = Arrangement.Top
    ){
        var isSheetEmergencyContactOpen by remember { mutableStateOf(false) }
        var isSheetDeleteAccountOpen by remember { mutableStateOf(false) }

        Box(modifier = Modifier
            .padding(horizontal = 16.dp)){
            Text(
                text = "Informasi Akun",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        }

        Spacer(Modifier.height(18.dp))

        ProfileOptionComponent(
            icon = Icons.Outlined.Person,
            title = "Kontak Darurat",
            onClick = { isSheetEmergencyContactOpen = true}
        )

        if (isSheetEmergencyContactOpen) {
           PopUp(
               isOpen = isSheetEmergencyContactOpen,
               onDismiss = { isSheetEmergencyContactOpen = false },
               title = "Kontak Darurat"
           ){
               // isi pop up
           }
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
            onClick = { isSheetDeleteAccountOpen = true }
        )

        if (isSheetDeleteAccountOpen) {
            PopUp(
                isOpen = isSheetDeleteAccountOpen,
                onDismiss = { isSheetDeleteAccountOpen = false },
                title = "Hapus Akun"
            ){
                // isi pop up
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(LocalGoogleAuthUiClient.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
                CustomTopAppBar(
                    pageTitle = "Profil",
                    icon = Icons.Filled.ArrowBack,
                )
        },
    ) { innerPadding ->
        // Konten Halaman Profil
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // First content: Container
                Container(
                    photoProfile = null,
                    userName = "Minjiaa",
                    isComplete = false
                )

                // Second content: ListOfOption
                ListOfOption()

                // Third content: LogoutButton
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ){
                    CustomButtonOutlined(
                        onClick = {
                            scope.launch {
                                viewModel.signOut()
                                Toast.makeText(
                                    context,
                                    "Berhasil logout",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        },
                        text = "Logout",
                        icon = painterResource(R.drawable.ic_logout),
                        textColor = Color.Black,
                        iconColor = Color.Red,
                        outlineColor = Color.Red,
                        )
                }

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