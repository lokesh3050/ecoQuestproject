package com.ecoquest.app.ui.screens.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.GamificationViewModel
import kotlin.math.roundToInt

data class WaterActivity(
    val id: String,
    val emoji: String,
    val name: String,
    val litersUsed: Int,
    val tip: String,
    val savingOption: String,
    val savedLiters: Int
)

private val activities = listOf(
    WaterActivity("shower",    "🚿", "Take a Shower",       80, "A 5-min shower uses ~60L. Try shorter showers!", "Cut shower by 3 min",  30),
    WaterActivity("bath",      "🛁", "Bath",                200, "A full bath uses ~200L. Swap for a quick shower!", "Shower instead",       140),
    WaterActivity("dishes",    "🍽️","Wash Dishes (tap)",   40, "Running tap wastes 12L/min. Fill a bowl instead!", "Use a bowl",           25),
    WaterActivity("laundry",   "👗", "Laundry",             70, "Full loads save water! Avoid half-loads.", "Full load only",       30),
    WaterActivity("drinking",  "🥤", "Drinking & Cooking",  8,  "Essential usage — can't reduce much here!", "Use filtered bottle",   2),
    WaterActivity("garden",    "🌱", "Water Garden",        50, "Water in the evening to reduce evaporation!", "Evening watering",     20),
    WaterActivity("car",       "🚗", "Wash Car",            150,"Use a bucket instead of a hose!", "Bucket wash",          120),
    WaterActivity("toilet",    "🚽","Flush Toilet",         30, "Dual-flush saves 3-4L per flush!", "Use dual flush",       12)
)

@Composable
fun WaterConservationGame(
    onBack: () -> Unit,
    gamViewModel: GamificationViewModel = viewModel()
) {
    var currentDay by remember { mutableIntStateOf(1) }
    val totalDays = 5
    val dailyTarget = 150
    var dayUsage by remember { mutableIntStateOf(0) }
    var selectedActivities by remember { mutableStateOf<Set<String>>(emptySet()) }
    var savedActivities by remember { mutableStateOf<Set<String>>(emptySet()) }
    var dayComplete by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var totalSaved by remember { mutableIntStateOf(0) }
    var dailyResults by remember { mutableStateOf<List<Pair<Int, Boolean>>>(emptyList()) }
    var rewardGiven by remember { mutableStateOf(false) }

    val baseUsage = activities.sumOf { it.litersUsed }
    val currentSavings = savedActivities.sumOf { id -> activities.find { it.id == id }?.savedLiters ?: 0 }
    dayUsage = baseUsage - currentSavings
    val underTarget = dayUsage <= dailyTarget

    if (gameOver) {
        val daysUnder = dailyResults.count { it.second }
        GameResultScreen(
            emoji = "💧",
            title = "Water Challenge Done!",
            score = daysUnder,
            total = totalDays,
            xpEarned = 100,
            epEarned = 100,
            gradientColors = listOf(Color(0xFF0277BD), Color(0xFF29B6F6)),
            onBack = onBack,
            onReplay = {
                currentDay = 1; dayUsage = 0; selectedActivities = emptySet()
                savedActivities = emptySet(); dayComplete = false; gameOver = false
                totalSaved = 0; dailyResults = emptyList(); rewardGiven = false
            }
        )
        return
    }

    Column(Modifier.fillMaxSize().background(CloudWhite)) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF0277BD), Color(0xFF29B6F6))),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack,
                        modifier = Modifier.size(36.dp).background(Color.White.copy(0.15f), CircleShape)) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text("💧 Water Conservation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Day $currentDay of $totalDays  •  Target: ≤${dailyTarget}L/day", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = if (underTarget) Color(0xFF4CAF50).copy(0.3f) else Color(0xFFEF5350).copy(0.3f)) {
                        Text("${dayUsage}L", Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Day progress dots
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..totalDays).forEach { d ->
                        val result = dailyResults.getOrNull(d - 1)
                        Box(
                            Modifier.size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        result != null -> if (result.second) Color(0xFF4CAF50) else Color(0xFFEF5350)
                                        d == currentDay -> Color.White
                                        else -> Color.White.copy(0.3f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                when {
                                    result != null -> if (result.second) "✓" else "✗"
                                    else -> "$d"
                                },
                                color = if (d == currentDay) Color(0xFF0277BD) else Color.White,
                                fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Usage gauge
            Spacer(Modifier.height(8.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Today's Usage", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepEarth)
                            Text("Target: ≤${dailyTarget}L  •  Saved: ${currentSavings}L", fontSize = 12.sp, color = MistGray)
                        }
                        Text("${dayUsage}L", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                            color = if (underTarget) Color(0xFF0277BD) else Color(0xFFEF5350))
                    }
                    Spacer(Modifier.height(12.dp))
                    val progress = (dayUsage.toFloat() / (dailyTarget * 1.5f)).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(7.dp)),
                        color = if (underTarget) Color(0xFF29B6F6) else Color(0xFFEF5350),
                        trackColor = Color(0xFFE3F2FD)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("0L", fontSize = 11.sp, color = MistGray)
                        Text("Target: ${dailyTarget}L", fontSize = 11.sp, color = if (underTarget) Color(0xFF0277BD) else Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                        Text("${(dailyTarget * 1.5f).roundToInt()}L+", fontSize = 11.sp, color = MistGray)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            if (!dayComplete) {
                Text("💡 Choose water-saving alternatives:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepEarth)
                Spacer(Modifier.height(4.dp))
                Text("Toggle activities you'll do today, then apply saving options to reduce usage.", fontSize = 12.sp, color = MistGray)
                Spacer(Modifier.height(12.dp))

                activities.forEach { act ->
                    val isSaved = act.id in savedActivities
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(if (isSaved) Color(0xFFE3F2FD) else Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(act.emoji, fontSize = 26.sp)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(act.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepEarth)
                                    Text("Base: ${act.litersUsed}L  ${if (isSaved) "→ Saving: ${act.savedLiters}L" else ""}", fontSize = 12.sp, color = MistGray)
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSaved) Color(0xFF29B6F6).copy(0.15f) else Color(0xFFF5F5F5),
                                    modifier = Modifier.clickable {
                                        savedActivities = if (isSaved)
                                            savedActivities - act.id
                                        else
                                            savedActivities + act.id
                                    }
                                ) {
                                    Text(
                                        if (isSaved) "✓ Saving" else "💧 Save",
                                        Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                        color = if (isSaved) Color(0xFF0277BD) else MistGray
                                    )
                                }
                            }
                            if (!isSaved) {
                                Spacer(Modifier.height(6.dp))
                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFFF8E1)) {
                                    Text("💡 ${act.tip}", Modifier.padding(8.dp), fontSize = 11.sp, color = Color(0xFF795548))
                                }
                            } else {
                                Spacer(Modifier.height(6.dp))
                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE3F2FD)) {
                                    Text("✅ ${act.savingOption} — saves ${act.savedLiters}L!", Modifier.padding(8.dp), fontSize = 11.sp, color = Color(0xFF0277BD), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        dayComplete = true
                        totalSaved += currentSavings
                        dailyResults = dailyResults + (dayUsage to underTarget)
                        if (currentDay == totalDays) {
                            if (!rewardGiven) {
                                rewardGiven = true
                                gamViewModel.addXP(100)
                                gamViewModel.addEcoPoints(100)
                                gamViewModel.recordActivity()
                            }
                            gameOver = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                ) {
                    Text("✅ Complete Day $currentDay", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                // Day summary
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(if (underTarget) Color(0xFFE3F2FD) else Color(0xFFFFEBEE)), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (underTarget) "🎉" else "😬", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(if (underTarget) "Day $currentDay — Under Target!" else "Day $currentDay — Over Target!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (underTarget) Color(0xFF0277BD) else Color(0xFFEF5350))
                        Text("Used: ${dayUsage}L  •  Saved: ${currentSavings}L", fontSize = 14.sp, color = MistGray)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                currentDay++; dayComplete = false; savedActivities = emptySet()
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                        ) { Text("▶ Day ${currentDay + 1}", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
