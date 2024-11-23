package com.herehearteam.herehear.ui.screens.archive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.ui.components.BottomNavigationBar
import com.herehearteam.herehear.ui.components.CustomSearchBar
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.DateBox
import com.herehearteam.herehear.ui.components.JournalBox
import com.herehearteam.herehear.ui.theme.ColorPrimary
import com.herehearteam.herehear.ui.theme.HereHearTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlin.math.absoluteValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MonthSelector(
    months: List<String> = listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"),
    selectedMonth: Int,
    onMonthSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedMonth,
        edgePadding = 16.dp,
        containerColor = Color.Transparent,
        divider = { },
        indicator = { },
        modifier = Modifier
            .padding(top = 45.dp)
    ) {
        months.forEachIndexed { index, month ->
            Surface(
                onClick = { onMonthSelected(index) },
                modifier = Modifier
                    .padding(4.dp)
                    .height(32.dp),
                color = if (selectedMonth == index) {
                    Color(0xFFFFEEBF)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(1.dp, Color(0xFFFFEEBF))
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = month,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}

// DummyData.kt
object DummyJournals {
    val journalList = listOf(
        Journal(
            id = "1",
            content = "Hari ini saya belajar Jetpack Compose. Sangat menyenangkan bisa membuat UI dengan mudah menggunakan Kotlin.",
            dateTime = LocalDateTime.now(),
        ),
        Journal(
            id = "2",
            content = "Mengerjakan project Android dan berdiskusi dengan tim tentang fitur baru yang akan ditambahkan.",
            dateTime = LocalDateTime.now(),
        ),
        Journal(
            id = "3",
            content = "Mempelajari tentang StateFlow dan SharedFlow di Kotlin Coroutines. Konsepnya cukup menarik!",
            dateTime = LocalDateTime.now().minusDays(2),
        ),
        Journal(
            id = "4",
            content = "Hari ini fokus debugging aplikasi dan memperbaiki beberapa UI yang masih belum sempurna.",
            dateTime = LocalDateTime.now().minusDays(3),
        ),
        Journal(
            id = "5",
            content = "Hari ini begadang sampai mampus jantung aku bahagia hidup bersamaku",
            dateTime = LocalDateTime.now().minusDays(3),
        )
    )
}

// JournalViewModel.kt
class ArchiveViewModel : ViewModel() {
    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals.asStateFlow()

    init {
        // Load dummy data saat ViewModel diinisialisasi
        loadDummyData()
    }

    private fun loadDummyData() {
        _journals.value = DummyJournals.journalList
    }
}

@Composable
fun ListOfJournal(
    modifier: Modifier = Modifier,
    journals: List<Journal> = emptyList(),
    selectedMonthIndex: Int
) {
    val colors = listOf(Color(0xFFBCF1F3), Color(0xFFFFEEBF), Color(0xFFDECDFD), Color(0xFFF3E5DF))
    val months = listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember")

    val selectedMonth = months[selectedMonthIndex]

    val filteredAndGroupedJournals = remember(journals, selectedMonthIndex) {
        journals
            .filter { journal ->
                val journalMonth = journal.dateTime.monthValue - 1
                journalMonth == selectedMonthIndex
            }
            .groupBy { journal ->
                // Mengelompokkan berdasarkan tanggal (tanpa waktu)
                journal.dateTime.toLocalDate()
            }
            .toSortedMap(reverseOrder()) // Mengurutkan tanggal dari yang terbaru
    }

    val journalColors = remember(journals) {
        journals.associate { journal ->
            journal.id to colors[journal.id.hashCode().absoluteValue % colors.size]
        }
    }

    Box(modifier = modifier.fillMaxSize()){
        if(filteredAndGroupedJournals.isEmpty()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Kamu belum menulis jurnal di bulan $selectedMonth",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                filteredAndGroupedJournals.forEach { (date, journalsForDate) ->
                    items(
                        items = journalsForDate,
                        key = { journal -> journal.id }
                    ) { journal ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.22f)
                                    .fillMaxWidth()
                            ) {
                                if (journal == journalsForDate.first()) {
                                    DateBox(dateTime = journal.dateTime)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(0.78f)
                                    .fillMaxWidth()
                            ) {
                                JournalBox(
                                    text = journal.content,
                                    color = journalColors[journal.id] ?: colors[0]
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Data class untuk journal
data class Journal(
    val id: String,
    val content: String,
    val dateTime: LocalDateTime,
)

@Composable
fun ArchiveScreen(navController: NavHostController, viewModel: ArchiveViewModel = viewModel())
{
    val journals = viewModel.journals.collectAsState().value
    var selectedMonth by remember { mutableStateOf(10) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(color = ColorPrimary)
                    .height(139.dp)

            ){Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ){
                CustomTopAppBar(
                    pageTitle = "Jurnal Ku",
                    backgroundColor = ColorPrimary,
                    contentColor = Color.White,
                    icon = Icons.Default.ArrowBack
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .offset(y = 34.dp)
                ){
                    CustomSearchBar(
                        onValueChange = {},
                        placeholder = "Cari Jurnalmu",
                        iconColor = ColorPrimary,
                        backgroundColor = Color(0xFFD1C4FD),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Filter")
                        }
                    )
                }

            }
            }
        },
//        bottomBar = {
//            BottomNavigationBar(navController = navController)
//        }
    ){ innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Column{
                MonthSelector(
                    selectedMonth = selectedMonth,
                    onMonthSelected = { index ->
                        selectedMonth = index  // Update state saat bulan dipilih
                    }
                )
                Box{
                    ListOfJournal(
                        journals = journals,
                        selectedMonthIndex = selectedMonth,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArchiveScreenPreview(){
    val navController = rememberNavController()
    HereHearTheme {
        ArchiveScreen(navController = navController)
    }
}