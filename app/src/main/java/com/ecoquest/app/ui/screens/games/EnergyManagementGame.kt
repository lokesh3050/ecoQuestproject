package com.ecoquest.app.ui.screens.games

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

data class Appliance(
    val id: String,
    val emoji: String,
    val name: String,
    val watts: Int,
    val category: String,
    val defaultHours: Float,
    val tip: String,
    val isEssential: Boolean = false
)

data class EnergyRoom(
    val id: String,
    val emoji: String,
    val name: String,
    val appliances: List<Appliance>,
    val budgetKwh: Float = 3.0f
)

private val rooms = listOf(
    EnergyRoom("bedroom", "🛏️", "Bedroom", listOf(
        Appliance("ac",      "❄️", "Air Conditioner", 1500, "Cooling",       5.0f, "Set to 24°C saves 6% energy per degree"),
        Appliance("fan",     "💨", "Ceiling Fan",      75,  "Cooling",       8.0f, "Fans use 10× less power than AC", true),
        Appliance("light",   "💡", "LED Light",         9,  "Lighting",      6.0f, "LED uses 75% less than incandescent", true),
        Appliance("tv",      "📺", "Television",       120, "Entertainment", 3.0f, "Streaming adds ~0.04 kWh/hr"),
        Appliance("charger", "🔌", "Phone Charger",    25,  "Electronics",   2.0f, "Unplug when full — saves 5-10 W", true)
    )),
    EnergyRoom("kitchen", "🍳", "Kitchen", listOf(
        Appliance("fridge",  "🧊", "Refrigerator",    150, "Appliance",     24.0f,"Keep at 4°C; don't overfill", true),
        Appliance("microwave","📡","Microwave",        1200,"Cooking",       0.5f, "Microwave 70% more efficient than oven"),
        Appliance("stove",   "🔥", "Electric Stove",  1500,"Cooking",       1.0f, "Cover pots to cook 25% faster"),
        Appliance("toaster", "🍞", "Toaster",          900,"Cooking",       0.2f, "Use sparingly; unplugged when idle"),
        Appliance("dishwash","🫧", "Dishwasher",      1500,"Appliance",     0.5f, "Run only full loads; saves water too")
    )),
    EnergyRoom("living",  "🛋️", "Living Room", listOf(
        Appliance("tv2",     "📺", "Smart TV",         100,"Entertainment", 4.0f, "Enable eco mode in TV settings"),
        Appliance("router",  "📶", "WiFi Router",       10,"Electronics",   24.0f,"Turn off at night to save ~0.06 kWh", true),
        Appliance("fan2",    "💨", "Stand Fan",         55,"Cooling",       6.0f, "1 speed lower = 30% less power", true),
        Appliance("lights2", "💡", "LED Lights (x3)",  27,"Lighting",      5.0f, "Use motion sensors or timers", true),
        Appliance("console", "🎮", "Game Console",     150,"Entertainment", 2.0f, "Enable power saving mode")
    )),
    EnergyRoom("bathroom","🚿", "Bathroom", listOf(
        Appliance("geyser",  "🌡️","Electric Geyser",  2000,"Heating",      1.0f, "Solar geyser saves ~1.5 kg CO₂/day"),
        Appliance("exhaust", "💨","Exhaust Fan",        30, "Ventilation",  0.5f, "Run only during/after shower", true),
        Appliance("blight",  "💡","Bathroom Light",     9, "Lighting",      1.0f, "Install motion sensor switch", true),
        Appliance("hairdryer","💇","Hair Dryer",       1800,"Grooming",     0.3f, "Air-dry when possible"),
        Appliance("washer",  "🫧","Washing Machine",  1500,"Appliance",    0.7f, "Cold wash saves 90% of laundry energy")
    ))
)

@Composable
fun EnergyManagementGame(
    onBack: () -> Unit,
    gamViewModel: GamificationViewModel = viewModel()
) {
    var currentRoomIndex by remember { mutableIntStateOf(0) }
    // Map of roomId -> Map of applianceId -> hoursOn
    var hourSettings by remember {
        mutableStateOf(
            rooms.associate { room ->
                room.id to room.appliances.associate { it.id to it.defaultHours }
            }
        )
    }
    var completedRooms by remember { mutableStateOf<Set<String>>(emptySet()) }
    var gameOver by remember { mutableStateOf(false) }
    var rewardGiven by remember { mutableStateOf(false) }

    if (gameOver) {
        val totalKwh = rooms.sumOf { room ->
            room.appliances.sumOf { app ->
                val h = hourSettings[room.id]?.get(app.id) ?: app.defaultHours
                (app.watts * h / 1000.0)
            }
        }.toFloat()
        val defaultKwh = rooms.sumOf { room ->
            room.appliances.sumOf { app -> (app.watts * app.defaultHours / 1000.0) }
        }.toFloat()
        val savedKwh = defaultKwh - totalKwh
        val underBudget = rooms.all { room ->
            val kwhForRoom = room.appliances.sumOf { app ->
                val h = hourSettings[room.id]?.get(app.id) ?: app.defaultHours
                (app.watts * h / 1000.0)
            }.toFloat()
            kwhForRoom <= room.budgetKwh
        }
        if (!rewardGiven) {
            rewardGiven = true
            gamViewModel.addXP(130)
            gamViewModel.addEcoPoints(130)
            gamViewModel.recordActivity()
        }
        EnergyResultScreen(
            totalKwh = totalKwh,
            savedKwh = savedKwh,
            underBudget = underBudget,
            onBack = onBack,
            onReplay = {
                currentRoomIndex = 0
                hourSettings = rooms.associate { room ->
                    room.id to room.appliances.associate { it.id to it.defaultHours }
                }
                completedRooms = emptySet()
                gameOver = false; rewardGiven = false
            }
        )
        return
    }

    val room = rooms[currentRoomIndex]
    val roomHours = hourSettings[room.id] ?: emptyMap()
    val roomKwh = room.appliances.sumOf { app ->
        val h = roomHours[app.id] ?: app.defaultHours
        (app.watts * h / 1000.0)
    }.toFloat()
    val underBudget = roomKwh <= room.budgetKwh

    Column(Modifier.fillMaxSize().background(CloudWhite)) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFFE65100), Color(0xFFFFA726))),
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
                        Text("⚡ Energy Management", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Room ${currentRoomIndex + 1}/${rooms.size} — ${room.emoji} ${room.name}", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = if (underBudget) Color(0xFF4CAF50).copy(0.35f) else Color(0xFFEF5350).copy(0.35f)) {
                        Text("${(roomKwh * 10).roundToInt() / 10f} kWh", Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Room dots
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rooms.forEachIndexed { i, r ->
                        Box(
                            Modifier.size(32.dp).clip(CircleShape)
                                .background(when {
                                    r.id in completedRooms -> Color(0xFF69F0AE)
                                    i == currentRoomIndex -> Color.White
                                    else -> Color.White.copy(0.3f)
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (r.id in completedRooms) "✓" else r.emoji,
                                fontSize = if (r.id in completedRooms) 14.sp else 14.sp,
                                color = if (i == currentRoomIndex) Color(0xFFE65100) else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Spacer(Modifier.height(8.dp))

            // Budget card
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("${room.emoji} ${room.name} Budget", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepEarth)
                            Text("Limit: ${room.budgetKwh} kWh/day", fontSize = 12.sp, color = MistGray)
                        }
                        Text(
                            "${(roomKwh * 10).roundToInt() / 10f}/${room.budgetKwh} kWh",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            color = if (underBudget) Color(0xFFE65100) else Color(0xFFEF5350)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    val progress = (roomKwh / (room.budgetKwh * 1.5f)).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(7.dp)),
                        color = if (underBudget) Color(0xFFFFA726) else Color(0xFFEF5350),
                        trackColor = Color(0xFFFFF3E0)
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("0 kWh", fontSize = 11.sp, color = MistGray)
                        Text(
                            if (underBudget) "✅ Under budget!" else "❌ Over budget — reduce hours!",
                            fontSize = 11.sp, color = if (underBudget) Color(0xFF4CAF50) else Color(0xFFEF5350),
                            fontWeight = FontWeight.Bold
                        )
                        Text("${room.budgetKwh}+ kWh", fontSize = 11.sp, color = MistGray)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Text("🔌 Adjust Daily Usage Hours", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepEarth)
            Spacer(Modifier.height(4.dp))
            Text("Slide to set how many hours each appliance runs per day.", fontSize = 12.sp, color = MistGray)
            Spacer(Modifier.height(12.dp))

            room.appliances.forEach { app ->
                val h = roomHours[app.id] ?: app.defaultHours
                val appKwh = (app.watts * h / 1000f)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(app.emoji, fontSize = 26.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(app.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepEarth)
                                    Text("${(appKwh * 100).roundToInt() / 100f} kWh/day", fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (appKwh < (app.watts * app.defaultHours / 1000f)) Color(0xFF4CAF50) else Color(0xFFE65100))
                                }
                                Text("${app.watts}W  •  ${(h * 10).roundToInt() / 10f}h/day", fontSize = 12.sp, color = MistGray)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Slider(
                            value = h,
                            onValueChange = { newH ->
                                val updated = (hourSettings[room.id] ?: emptyMap()).toMutableMap()
                                updated[app.id] = newH
                                hourSettings = hourSettings.toMutableMap().also { it[room.id] = updated }
                            },
                            valueRange = if (app.isEssential) 0.5f..24f else 0f..24f,
                            steps = 23,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFFFA726),
                                activeTrackColor = Color(0xFFE65100),
                                inactiveTrackColor = Color(0xFFFFF3E0)
                            )
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${if (app.isEssential) "0.5" else "0"}h", fontSize = 10.sp, color = MistGray)
                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFFF8E1)) {
                                Text("💡 ${app.tip}", Modifier.padding(6.dp), fontSize = 10.sp, color = Color(0xFF795548))
                            }
                            Text("24h", fontSize = 10.sp, color = MistGray)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    completedRooms = completedRooms + room.id
                    if (currentRoomIndex < rooms.size - 1) {
                        currentRoomIndex++
                    } else {
                        gameOver = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFE65100))
            ) {
                Text(
                    if (currentRoomIndex < rooms.size - 1) "✅ Confirm ${room.name} →" else "🏁 See Results",
                    fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EnergyResultScreen(
    totalKwh: Float,
    savedKwh: Float,
    underBudget: Boolean,
    onBack: () -> Unit,
    onReplay: () -> Unit
) {
    val co2Saved = savedKwh * 0.82f // India grid emission factor ~0.82 kg CO2/kWh
    val costSaved = savedKwh * 7.5f // avg ₹7.5/unit

    Column(Modifier.fillMaxSize().background(CloudWhite).verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color(0xFFE65100), Color(0xFFFFA726))),
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (underBudget) "⚡🌟" else "⚡⚠️", fontSize = 52.sp)
                Spacer(Modifier.height(8.dp))
                Text(if (underBudget) "All Rooms Optimised!" else "Energy Check Complete!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(4.dp))
                Text("Total: ${(totalKwh * 10).roundToInt() / 10f} kWh/day", color = Color.White.copy(0.9f), fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(0.2f)) {
                        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${(savedKwh * 10).roundToInt() / 10f}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("kWh saved", color = Color.White.copy(0.8f), fontSize = 11.sp)
                        }
                    }
                    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(0.2f)) {
                        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${(co2Saved * 10).roundToInt() / 10f}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("kg CO₂ less", color = Color.White.copy(0.8f), fontSize = 11.sp)
                        }
                    }
                    Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(0.2f)) {
                        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("₹${(costSaved * 10).roundToInt() / 10f}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("saved/day", color = Color.White.copy(0.8f), fontSize = 11.sp)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("⭐ +130 XP  •  🌱 +130 Eco Points", color = Color.White, fontSize = 13.sp)
            }
        }

        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(16.dp))
            // Per-room summary
            Text("🏠 Room Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepEarth)
            Spacer(Modifier.height(10.dp))
            rooms.forEach { room ->
                val rKwh = room.appliances.sumOf { (it.watts * it.defaultHours / 1000.0) }.toFloat()
                val isUnder = rKwh <= room.budgetKwh
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(if (isUnder) Color(0xFFFFF3E0) else Color(0xFFFFEBEE)), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(room.emoji, fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(room.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepEarth)
                            Text("Budget: ${room.budgetKwh} kWh/day", fontSize = 12.sp, color = MistGray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${(rKwh * 10).roundToInt() / 10f} kWh", fontWeight = FontWeight.Bold, fontSize = 14.sp,
                                color = if (isUnder) Color(0xFFE65100) else Color(0xFFEF5350))
                            Text(if (isUnder) "✅ OK" else "❌ Over", fontSize = 12.sp, color = if (isUnder) Color(0xFF4CAF50) else Color(0xFFEF5350))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            // Eco fact
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF3E0)) {
                Row(Modifier.padding(16.dp)) {
                    Text("⚡", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Did You Know?", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFE65100))
                        Spacer(Modifier.height(4.dp))
                        Text("If every Indian household saved 1 kWh/day, we'd collectively avoid ~950,000 tonnes of CO₂ daily — equivalent to planting 43 million trees!", fontSize = 12.sp, color = Color(0xFF795548))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp)) { Text("← Back") }
                Button(onClick = onReplay, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(Color(0xFFE65100))) { Text("🔄 Try Again") }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
