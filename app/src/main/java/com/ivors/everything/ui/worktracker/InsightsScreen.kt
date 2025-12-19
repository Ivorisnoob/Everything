package com.ivors.everything.ui.worktracker

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarViewMonth
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import java.util.SortedMap
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InsightsScreen(
    viewModel: WorkTrackerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRangeIndex by remember { mutableIntStateOf(0) }
    val rangeTypes = listOf(RangeType.Week, RangeType.Month, RangeType.Year, RangeType.Custom)
    val rangeType = rangeTypes[selectedRangeIndex]
    
    var currentRangeStart by remember { 
        mutableStateOf(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) 
    }
    var customRangeEnd by remember { mutableStateOf(LocalDate.now()) }
    
    val endDate = remember(rangeType, currentRangeStart, customRangeEnd) {
        when (rangeType) {
            RangeType.Week -> currentRangeStart.plusDays(6)
            RangeType.Month -> currentRangeStart.plusMonths(1).minusDays(1)
            RangeType.Year -> currentRangeStart.plusYears(1).minusDays(1)
            RangeType.Custom -> customRangeEnd
        }
    }
    
    var stats by remember { mutableStateOf<Map<LocalDate, Double>>(emptyMap()) }
    
    LaunchedEffect(currentRangeStart, endDate, rangeType) {
        viewModel.getLogsInRange(currentRangeStart, endDate).collect {
            stats = it
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { 
                    Text(
                        text = "Insights",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                subtitle = {
                    Text(
                        text = "Your productivity journey",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                titleHorizontalAlignment = Alignment.Start,
                navigationIcon = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text("Go back") } },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 32.dp
            )
        ) {
            // Range Selector using SingleChoiceSegmentedButtonRow
            item {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    rangeTypes.forEachIndexed { index, type ->
                        SegmentedButton(
                            selected = selectedRangeIndex == index,
                            onClick = { 
                                selectedRangeIndex = index
                                when (type) {
                                    RangeType.Week -> {
                                        currentRangeStart = LocalDate.now()
                                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                    }
                                    RangeType.Month -> {
                                        currentRangeStart = LocalDate.now().withDayOfMonth(1)
                                    }
                                    RangeType.Year -> {
                                        currentRangeStart = LocalDate.now().withDayOfYear(1)
                                    }
                                    RangeType.Custom -> {
                                        showDatePicker = true
                                    }
                                }
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = rangeTypes.size
                            ),
                            icon = {
                                SegmentedButtonDefaults.Icon(active = selectedRangeIndex == index) {
                                    Icon(
                                        imageVector = when (type) {
                                            RangeType.Week -> Icons.Outlined.CalendarViewWeek
                                            RangeType.Month -> Icons.Outlined.CalendarViewMonth
                                            RangeType.Year -> Icons.Outlined.DateRange
                                            RangeType.Custom -> Icons.Outlined.EditCalendar
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                    )
                                }
                            }
                        ) {
                            Text(type.name)
                        }
                    }
                }
            }
            
            // Date Range Display Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    onClick = { if (rangeType == RangeType.Custom) showDatePicker = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (rangeType == RangeType.Month) 
                                currentRangeStart.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                            else if (rangeType == RangeType.Year)
                                currentRangeStart.format(DateTimeFormatter.ofPattern("yyyy"))
                            else
                                "${currentRangeStart.format(DateTimeFormatter.ofPattern("MMM d"))} - ${endDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Stats Chart
            item {
                InsightsChart(
                    stats = stats,
                    rangeType = rangeType,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Summary Card
            item {
                SummaryCard(
                    stats = stats,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    
    // Date Range Picker Dialog
    if (showDatePicker) {
        val dateRangePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = currentRangeStart
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            initialSelectedEndDateMillis = customRangeEnd
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateRangePickerState.selectedStartDateMillis?.let { startMillis ->
                            currentRangeStart = Instant.ofEpochMilli(startMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        dateRangePickerState.selectedEndDateMillis?.let { endMillis ->
                            customRangeEnd = Instant.ofEpochMilli(endMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(28.dp)
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                title = {
                    Text(
                        text = "Select Date Range",
                        modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 12.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                headline = {
                    Text(
                        text = "Range Selection",
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun InsightsChart(
    stats: Map<LocalDate, Double>,
    rangeType: RangeType,
    modifier: Modifier = Modifier
) {
    val totalHours = stats.values.sum()
    val maxHours = (stats.values.maxOrNull() ?: 1.0).coerceAtLeast(8.0)
    
    val expectedHours = when (rangeType) {
        RangeType.Week -> 40.0
        RangeType.Month -> 160.0
        RangeType.Year -> 1920.0
        RangeType.Custom -> (stats.size * 8).toDouble().coerceAtLeast(8.0)
    }
    val progressRatio = (totalHours / expectedHours).coerceIn(0.0, 1.0).toFloat()
    
    val animatedProgress by animateFloatAsState(
        targetValue = progressRatio,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress"
    )
    
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${rangeType.name} Productivity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${stats.size} records found",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            val strokeWidthPx = with(LocalDensity.current) { 12.dp.toPx() }
            val thickStroke = remember(strokeWidthPx) {
                Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            }
            
            LinearWavyProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                stroke = thickStroke,
                trackStroke = thickStroke
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}% achieved",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Target: ${String.format(Locale.getDefault(), "%.0f", expectedHours)}h",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (stats.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                
                val sortedStats = stats.toSortedMap()
                val displayStats = if (stats.size > 14) {
                    val entries = sortedStats.entries.toList()
                    (entries.take(7) + entries.takeLast(7)).associate { it.key to it.value }
                } else {
                    sortedStats
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    displayStats.forEach { (date, hours) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            val heightRatio = (hours / maxHours).toFloat()
                            val isToday = date == LocalDate.now()
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(heightRatio.coerceAtLeast(0.05f))
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        if (isToday)
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.tertiary
                                                )
                                            )
                                        else
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                                )
                                            )
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (rangeType) {
                                    RangeType.Week -> date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
                                    RangeType.Month -> date.dayOfMonth.toString()
                                    else -> date.format(DateTimeFormatter.ofPattern("d"))
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                color = if (isToday) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SummaryCard(
    stats: Map<LocalDate, Double>,
    modifier: Modifier = Modifier
) {
    val totalHours = stats.values.sum()
    val avgHours = if (stats.isNotEmpty()) totalHours / stats.size else 0.0
    val maxHours = stats.values.maxOrNull() ?: 0.0
    
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Analytics,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Performance Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = String.format(Locale.getDefault(), "%.1f", totalHours),
                    label = "Total Hours",
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    value = String.format(Locale.getDefault(), "%.1f", avgHours),
                    label = "Average/Day",
                    color = MaterialTheme.colorScheme.secondary
                )
                StatItem(
                    value = String.format(Locale.getDefault(), "%.1f", maxHours),
                    label = "Peak Day",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

enum class RangeType {
    Week, Month, Year, Custom
}
