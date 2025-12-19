package com.ivors.everything.ui.worktracker

import kotlinx.coroutines.launch

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ivors.everything.data.WorkLog
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.SortedMap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WorkTrackerScreen(
    viewModel: WorkTrackerViewModel,
    onNavigateToInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val logs by viewModel.logsForSelectedDay.collectAsState()
    val weeklyHours by viewModel.weeklyWorkHours.collectAsState()
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    
    // Sync date picker selection with ViewModel
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val newDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
            if (newDate != selectedDate) {
                viewModel.selectDate(newDate)
            }
        }
    }
    
    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(
                        text = "Work Tracker",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                subtitle = {
                    Text(
                        text = "Insights & Logs",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                titleHorizontalAlignment = Alignment.Start,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent, // Let the background show through
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface // Standard scaffold color
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                ),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 32.dp,
                start = 0.dp,
                end = 0.dp
            )
        ) {
            // Insights Section
            item {
                WorkInsights(
                    weeklyHours = weeklyHours,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Action Buttons using ButtonGroup (Expressive Layout)
            item {
                ExpressiveActions(
                    onWalkIn = { viewModel.walkIn() },
                    onWalkOut = { viewModel.walkOut() },
                    onViewInsights = onNavigateToInsights,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Calendar Card
            item {
                var showCalendar by remember { mutableStateOf(false) }
                ElevatedCard(
                    onClick = { showCalendar = !showCalendar },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = selectedDate.dayOfMonth.toString(),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Badge(
                                containerColor = if (showCalendar) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.primaryContainer,
                                contentColor = if (showCalendar)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onPrimaryContainer
                            ) {
                                Text(
                                    text = if (showCalendar) "Hide" else "Change",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        
                        if (showCalendar) {
                            DatePicker(
                                state = datePickerState,
                                modifier = Modifier.fillMaxWidth(),
                                title = null,
                                headline = null,
                                showModeToggle = false,
                                colors = DatePickerDefaults.colors(
                                    containerColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
            
            // Logs Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activity Log",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text(
                            text = "${logs.size} events",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            
            if (logs.isEmpty()) {
                item {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Outlined.Analytics,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No logs recorded for this day",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                items(logs) { log ->
                    WorkLogItem(
                        log = log,
                        onDelete = { viewModel.deleteLog(log.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WorkInsights(
    weeklyHours: Map<LocalDate, Double>,
    modifier: Modifier = Modifier
) {
    val totalHours = weeklyHours.values.sum()
    val maxHours = (weeklyHours.values.maxOrNull() ?: 1.0).coerceAtLeast(8.0)
    val progressRatio = (totalHours / 40.0).coerceIn(0.0, 1.0).toFloat()
    
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
                    modifier = Modifier.size(40.dp)
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
                        text = "Weekly Efficiency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goal: 40 hours",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val strokeWidthPx = with(LocalDensity.current) { 10.dp.toPx() }
            val thickStroke = remember(strokeWidthPx) {
                Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            }
            
            LinearWavyProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                stroke = thickStroke,
                trackStroke = thickStroke
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "${(animatedProgress * 100).toInt()}% towards goal",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Bar Chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyHours.toSortedMap().forEach { (date, hours) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        val heightRatio = (hours / maxHours).toFloat()
                        val isToday = date == LocalDate.now()
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .fillMaxHeight(heightRatio.coerceAtLeast(0.05f))
                                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                .background(
                                    if (isToday)
                                        Brush.verticalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        )
                                    else
                                        Brush.verticalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                            )
                                        )
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveActions(
    onWalkIn: () -> Unit,
    onWalkOut: () -> Unit,
    onViewInsights: () -> Unit,
    modifier: Modifier = Modifier
) {
    var walkingIn by remember { mutableStateOf(false) }
    var walkingOut by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        // Proper ButtonGroup with connected ToggleButtons as per M3 docs
        ButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
        ) {
            // Using ToggleButton with connected shapes and animateWidth for expressive feedback
            ToggleButton(
                checked = walkingIn,
                onCheckedChange = { 
                    walkingIn = true
                    onWalkIn()
                    scope.launch {
                        kotlinx.coroutines.delay(800)
                        walkingIn = false
                    }
                },
                modifier = Modifier
                    .weight(1.2f)
                    .heightIn(min = ButtonDefaults.LargeContainerHeight),
                shapes = ButtonGroupDefaults.connectedLeadingButtonShapes(),
                colors = ToggleButtonDefaults.toggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = if (walkingIn) Icons.Filled.Login else Icons.Outlined.Login,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.LargeContainerHeight))
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.LargeContainerHeight)))
                Text(
                    text = "Walk In",
                    style = ButtonDefaults.textStyleFor(ButtonDefaults.LargeContainerHeight),
                    fontWeight = FontWeight.Bold
                )
            }

            ToggleButton(
                checked = walkingOut,
                onCheckedChange = {
                    walkingOut = true
                    onWalkOut()
                    scope.launch {
                        kotlinx.coroutines.delay(800)
                        walkingOut = false
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = ButtonDefaults.LargeContainerHeight),
                shapes = ButtonGroupDefaults.connectedTrailingButtonShapes(),
                colors = ToggleButtonDefaults.toggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    checkedContainerColor = MaterialTheme.colorScheme.secondary,
                    checkedContentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(
                    imageVector = if (walkingOut) Icons.AutoMirrored.Filled.ExitToApp else Icons.Outlined.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.LargeContainerHeight))
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.LargeContainerHeight)))
                Text(
                    text = "Out",
                    style = ButtonDefaults.textStyleFor(ButtonDefaults.LargeContainerHeight),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Expressive Button with shape morphing for the main navigation action
        Button(
            onClick = onViewInsights,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = ButtonDefaults.MediumContainerHeight),
            shapes = ButtonDefaults.shapes(), // This enables expressive shape morphing
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MediumContainerHeight))
            )
            Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MediumContainerHeight)))
            Text(
                text = "View Analytics & Insights",
                style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight).copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WorkLogItem(
    log: WorkLog,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val time = Instant.ofEpochMilli(log.timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(timeFormatter)
    
    val isWalkIn = log.eventType == "IN"
    
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = if (isWalkIn) "Shift Started" else "Shift Ended",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = "at $time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    color = if (isWalkIn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isWalkIn) Icons.Filled.Login else Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = if (isWalkIn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            trailingContent = {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                        .size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
