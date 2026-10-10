package com.grinding.app.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.grinding.app.R
import com.grinding.app.data.model.TaskDto
import com.grinding.app.ui.components.CategorySelector
import com.grinding.app.ui.components.defaultCategories
import com.grinding.app.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddEditScreen(
    taskId: String? = null,
    existingTask: TaskDto? = null,
    onNavigateBack: () -> Unit,
    onSaveTask: (title: String, estimatedPomodoros: Int, date: LocalDate, color: String, startTime: String?, endTime: String?) -> Unit,
    onUpdateTask: (TaskDto) -> Unit = {},
    onDeleteTask: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var selectedCategory by remember {
        mutableStateOf(
            defaultCategories.find { it.colorKey == existingTask?.color }?.name ?: "Desain"
        )
    }
    var selectedColorKey by remember {
        mutableStateOf(existingTask?.color ?: "pink")
    }
    var estimatedPomodoros by remember { mutableIntStateOf(existingTask?.estimatedPomodoros ?: 3) }
    var sessionDurationMinutes by remember { mutableIntStateOf(25) }
    var targetDate by remember {
        mutableStateOf(
            if (existingTask?.taskDate != null) {
                try { LocalDate.parse(existingTask.taskDate) } catch (e: Exception) { LocalDate.now() }
            } else LocalDate.now()
        )
    }
    var startTime by remember { mutableStateOf(existingTask?.startTime ?: "09:00") }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val isEditing = existingTask != null || taskId != null

    // Date Picker Dialog
    if (showDatePickerDialog) {
        val initialMillis = targetDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            targetDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        showDatePickerDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
                    shape = PillShape
                ) {
                    Text(stringResource(R.string.action_pilih), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text(stringResource(R.string.action_batal), color = OnSurfaceVariant)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Primary,
                    selectedDayContentColor = OnPrimary,
                    todayDateBorderColor = Primary
                )
            )
        }
    }

    // Time Picker Dialog
    if (showTimePickerDialog) {
        val initialHour = try {
            startTime.split(":")[0].trim().toInt()
        } catch (e: Exception) { 9 }
        val initialMinute = try {
            startTime.split(":")[1].trim().take(2).toInt()
        } catch (e: Exception) { 0 }

        val timePickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )

        TimePickerDialog(
            title = stringResource(R.string.task_dialog_pick_time),
            onCancel = { showTimePickerDialog = false },
            onConfirm = {
                startTime = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                showTimePickerDialog = false
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialSelectedContentColor = OnPrimary,
                    clockDialColor = SurfaceContainerHigh,
                    selectorColor = Primary,
                    timeSelectorSelectedContainerColor = PrimaryContainer,
                    timeSelectorSelectedContentColor = OnPrimaryFixed
                )
            )
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Surface.copy(alpha = 0.95f),
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.action_kembali),
                                tint = OnSurface
                            )
                        }
                        Text(
                            text = if (isEditing) stringResource(R.string.task_title_detail) else stringResource(R.string.task_title_create),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }

                    Surface(
                        shape = PillShape,
                        color = SurfaceContainerHigh,
                        modifier = Modifier
                            .clip(PillShape)
                            .clickable {
                                if (title.isNotBlank()) {
                                    if (isEditing && existingTask != null) {
                                        onUpdateTask(
                                            existingTask.copy(
                                                title = title,
                                                estimatedPomodoros = estimatedPomodoros,
                                                taskDate = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                                color = selectedColorKey,
                                                startTime = startTime
                                            )
                                        )
                                    } else {
                                        onSaveTask(
                                            title,
                                            estimatedPomodoros,
                                            targetDate,
                                            selectedColorKey,
                                            startTime,
                                            null
                                        )
                                    }
                                    onNavigateBack()
                                }
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.action_simpan_cepat),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        },
        containerColor = Background,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode Banner
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer)
                    )
                    Text(
                        text = stringResource(R.string.task_mode_planning),
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceVariant
                    )
                }
            }

            // 1. Input Nama Tugas Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.task_label_activity).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text(stringResource(R.string.task_hint_activity)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = PillShape
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = Outline,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(R.string.task_helper_activity),
                                style = MaterialTheme.typography.bodySmall,
                                color = Outline
                            )
                        }
                    }
                }
            }

            // 2. Kategori / Label Warna
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.task_label_category),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = stringResource(R.string.task_category_selected, selectedCategory),
                                style = MaterialTheme.typography.labelSmall,
                                color = Primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        CategorySelector(
                            selectedCategory = selectedCategory,
                            onCategorySelected = { cat ->
                                selectedCategory = cat.name
                                selectedColorKey = cat.colorKey
                            }
                        )
                    }
                }
            }

            // 3. Estimasi Pomodoro Stepper
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.task_label_pomodoro_estimate),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Primary
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = SurfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (estimatedPomodoros > 1) estimatedPomodoros-- },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceContainerLowest)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = null, tint = OnSurface)
                                }

                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "$estimatedPomodoros",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.task_session_unit),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = OnSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { estimatedPomodoros++ },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceContainerLowest)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = OnSurface)
                                }
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = SurfaceContainer,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(R.string.task_total_estimate_format, estimatedPomodoros * sessionDurationMinutes),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 4. Durasi per Sesi
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.task_label_session_duration),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = stringResource(R.string.task_helper_session_duration),
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val durations = listOf(15, 25, 45, 60)
                            durations.forEach { dur ->
                                val isSelected = sessionDurationMinutes == dur
                                Surface(
                                    shape = PillShape,
                                    color = if (isSelected) Primary else SurfaceContainerLow,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(PillShape)
                                        .clickable { sessionDurationMinutes = dur }
                                ) {
                                    Text(
                                        text = "${dur}m",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) OnPrimary else OnSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Jadwal & Waktu
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.task_label_schedule),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Target Date Card (Interactive Clickable DatePicker)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceContainerLow,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { showDatePickerDialog = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = stringResource(R.string.task_label_target_date),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Outline
                                        )
                                        val df = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
                                        Text(
                                            text = targetDate.format(df),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSurface
                                        )
                                    }
                                }
                            }

                            // Start Time Card (Interactive Clickable TimePicker)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceContainerLow,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { showTimePickerDialog = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Alarm,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = stringResource(R.string.task_label_session_time),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Outline
                                        )
                                        Text(
                                            text = "$startTime WIB",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = OnSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Action Button (Save)
            item {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            if (isEditing && existingTask != null) {
                                onUpdateTask(
                                    existingTask.copy(
                                        title = title,
                                        estimatedPomodoros = estimatedPomodoros,
                                        taskDate = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        color = selectedColorKey,
                                        startTime = startTime
                                    )
                                )
                            } else {
                                onSaveTask(
                                    title,
                                    estimatedPomodoros,
                                    targetDate,
                                    selectedColorKey,
                                    startTime,
                                    null
                                )
                            }
                            onNavigateBack()
                        }
                    },
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.task_btn_save),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 7. Delete Button (if editing)
            if (isEditing && existingTask?.id != null) {
                item {
                    OutlinedButton(
                        onClick = {
                            onDeleteTask(existingTask.id)
                            onNavigateBack()
                        },
                        shape = PillShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = ErrorColor)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.action_hapus),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Pilih Waktu Sesi",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceContainerLowest,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(shape = RoundedCornerShape(24.dp), color = SurfaceContainerLowest)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancel) {
                        Text(stringResource(R.string.action_batal), color = OnSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary),
                        shape = PillShape
                    ) {
                        Text(stringResource(R.string.action_pilih), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
