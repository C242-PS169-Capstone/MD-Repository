package com.herehearteam.herehear.ui.screens.archive

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.util.Calendar
import android.widget.CalendarView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import com.herehearteam.herehear.data.local.repository.JournalRepository
import java.time.format.DateTimeFormatter
import java.util.*
import com.herehearteam.herehear.domain.model.Journal
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.herehearteam.herehear.navigation.Screen

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
                            fontSize = 14.sp,
                            color = if (isSystemInDarkTheme()) {
                                Color.White // Warna putih untuk mode gelap
                            } else {
                                Color.Black // Warna hitam untuk mode terang
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun JournalArchiveContent(
    modifier: Modifier = Modifier,
    journals: List<Journal> = emptyList(),
    selectedMonth: Int,
    onMonthSelected: (Int) -> Unit,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val colors = listOf(Color(0xFFBCF1F3), Color(0xFFFFEEBF), Color(0xFFDECDFD), Color(0xFFF3E5DF))
    val months = listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember")

    val filteredAndGroupedJournals = remember(journals, selectedMonth, selectedDate, searchQuery) {
        journals
            .filter { journal ->
                val journalMonth = journal.dateTime.monthValue - 1
                val journalDate = journal.dateTime.toLocalDate()
                val matchesMonth = journalMonth == selectedMonth
                val matchesDate = if (selectedDate != null) {
                    journalDate == selectedDate
                } else true
                val matchesSearch = if (searchQuery.isNotBlank()) {
                    journal.content.contains(searchQuery, ignoreCase = true)
                } else true

                matchesMonth && matchesDate && matchesSearch
            }
            .groupBy { journal ->
                journal.dateTime.toLocalDate()
            }
            .toSortedMap(reverseOrder())
    }

    val journalColors = remember(journals) {
        journals.associate { journal ->
            journal.id to colors[journal.id.hashCode().absoluteValue % colors.size]
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        // Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorPrimary)
                    .height(80.dp)
            ) {
                Column(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = 50.dp)){
                    CustomSearchBar(
                        value = searchQuery,
                        onValueChange = { newQuery ->
                            onSearchQueryChange(newQuery)
                        },
                        placeholder = "Cari Jurnalmu",
                        iconColor = ColorPrimary,
                        backgroundColor = Color(0xFFD1C4FD),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date Filter",
                                modifier = Modifier.clickable {
                                    showDatePicker = true
                                }
                            )
                        }
                    )
                }
            }
        }

        // Selected Date Chip (only shown when date is selected)
        if (selectedDate != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 40.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        color = Color(0xFFF3E5DF),
                        modifier = Modifier
                            .padding(4.dp)
                            .height(32.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = selectedDate?.format(
                                    DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id"))
                                ) ?: "",
                                style = TextStyle(
                                    color = if (isSystemInDarkTheme()) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    },
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear date",
                                tint = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                },
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        onDateSelected(null)
                                    }
                            )
                        }
                    }
                }
            }
        }

        if (selectedDate == null) {
            // Month Selector
            item {
                MonthSelector(
                    selectedMonth = selectedMonth,
                    onMonthSelected = onMonthSelected
                )
            }
        }

        // Empty State
        if (filteredAndGroupedJournals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        modifier = Modifier.offset(y = 128.dp),
                        text = when {
                            searchQuery.isNotBlank() -> "Tidak ada jurnal yang cocok dengan pencarian"
                            selectedDate != null -> "Tidak ada jurnal pada tanggal ini"
                            else -> "Kamu belum menulis jurnal di bulan ${months[selectedMonth]}"
                        },
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Journal Items
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
                                color = journalColors[journal.id] ?: colors[0],
                                journalId = journal.id,
                                onClick = {
                                    Log.d("ArchivSSSSSSSeScreen", "Journal clicked: ${journal.id}")
                                    navController.navigate(Screen.Journal.createRoute(journalId = journal.id)) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                onDateSelected(date)
                // Update selected month to match selected date
                onMonthSelected(date.monthValue - 1)
                showDatePicker = false
            }
        )
    }
}

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val calendar = Calendar.getInstance()
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            dateTextAppearance = android.R.style.TextAppearance_Medium
                            setOnDateChangeListener { _, year, month, dayOfMonth ->
                                onDateSelected(
                                    LocalDate.of(year, month + 1, dayOfMonth)
                                )
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun ArchiveScreen(
    navController: NavHostController,
    application: android.app.Application) {
    val context = LocalContext.current
    val viewModel: ArchiveViewModel = viewModel(
        factory = ArchiveViewModelFactory.getInstance(context)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier
                .background(ColorPrimary)){
                Column(modifier = Modifier
                    .padding(horizontal = 16.dp)){
                    CustomTopAppBar(
                        pageTitle = "JurnalKu",
                        backgroundColor = ColorPrimary,
                        contentColor = Color.White,
                        icon = Icons.Default.ArrowBack
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            JournalArchiveContent(
                journals = uiState.journals,
                selectedMonth = uiState.selectedMonth,
                onMonthSelected = { month ->
                    viewModel.updateSelectedMonth(month)
                },
                selectedDate = uiState.selectedDate,
                onDateSelected = { date ->
                    viewModel.updateSelectedDate(date)
                },
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.updateSearchQuery(query)
                },
                navController = navController
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ArchiveScreenPreview(){
//    val navController = rememberNavController()
//    val viewModel = ArchiveViewModel()
//    HereHearTheme {
//        ArchiveScreen(
//            navController = navController,
//            viewModel = viewModel)
//    }
//}