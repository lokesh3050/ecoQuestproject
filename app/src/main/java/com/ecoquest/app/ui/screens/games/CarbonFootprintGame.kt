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

data class CarbonCategory(val id: String, val emoji: String, val label: String, val color: Color)
data class CarbonOption(val label: String, val kgCO2: Float, val tip: String)

private val categories = listOf(
    CarbonCategory("transport", "🚗", "Transport",   Color(0xFF1B5E20)),
    CarbonCategory("food",      "🍽️","Food",         Color(0xFF2E7D32)),
    CarbonCategory("home",      "🏠", "Home Energy", Color(0xFF43A047)),
    CarbonCategory("shopping",  "🛍️","Shopping",    Color(0xFF66BB6A))
)

private val transportOptions = listOf(
    CarbonOption("Drive petrol car daily",   4.2f,  "Switch to public transport — saves ~3.5 kg CO₂/day"),
    CarbonOption("Public bus / metro",        0.7f,  "Great choice! Bus/metro is ~6× less carbon than driving alone."),
    CarbonOption("Cycle or walk",             0.0f,  "Zero-carbon commute — best possible choice! 🌿"),
    CarbonOption("Electric car",              1.2f,  "EVs cut ~65% emissions vs petrol cars on India's grid."),
    CarbonOption("Two-wheeler (petrol)",      1.8f,  "Better than car; switching to EV scooter halves this further.")
)

private val foodOptions = listOf(
    CarbonOption("Meat every meal",          3.3f,  "Beef produces 27 kg CO₂/kg. Try plant-rich meals!"),
    CarbonOption("Chicken + dairy",          1.7f,  "Chicken is ~10× lower impact than beef. Good step!"),
    CarbonOption("Mostly vegetarian",        0.9f,  "Plant-based diets use ~50% less carbon than meat-heavy diets."),
    CarbonOption("Vegan diet",               0.5f,  "Lowest food footprint — saves ~1.5 tonnes CO₂/year!"),
    CarbonOption("Local seasonal food",      0.7f,  "Local & seasonal food cuts transport & storage emissions.")
)

private val homeOptions = listOf(
    CarbonOption("AC + geysers all day",     5.0f,  "ACs account for ~40% of home energy. Use inverter AC at 24 °C."),
    CarbonOption("Moderate AC + LED bulbs",  2.5f,  "LEDs use 75% less energy than incandescent bulbs. 👍"),
    CarbonOption("Fans + solar water heater",1.0f,  "Solar geysers save ~1.5 kg CO₂/day vs electric geysers!"),
    CarbonOption("Solar panels installed",   0.3f,  "Solar panels can offset ~90% of home electricity emissions."),
    CarbonOption("Minimal appliances",       0.8f,  "Energy-efficient appliances (BEE 5-star) cut usage by 30-50%.")
)

private val shoppingOptions = listOf(
    CarbonOption("Buy new frequently",       2.5f,  "Fast fashion alone contributes 10% of global CO₂. Buy less!"),
    CarbonOption("Buy occasionally",         1.2f,  "Extending clothing life by 9 months cuts carbon by 20-30%."),
    CarbonOption("Second-hand / thrift",     0.4f,  "Second-hand shopping saves ~82% of the item's carbon footprint!"),
    CarbonOption("Minimal consumption",      0.2f,  "The most sustainable product is one you don't buy. 🌿"),
    CarbonOption("Repair & reuse",           0.3f,  "Repair culture extends product life and keeps waste out of landfills.")
)

private val optionsByCategory = mapOf(
    "transport" to transportOptions,
    "food"      to foodOptions,
    "home"      to homeOptions,
    "shopping"  to shoppingOptions
)

@Composable
fun CarbonFootprintGame(
    onBack: () -> Unit,
    gamViewModel: GamificationViewModel = viewModel()
) {
    var step by remember { mutableIntStateOf(0) } // 0-3 = categories, 4 = result
    var selections by remember { mutableStateOf<Map<String, CarbonOption>>(emptyMap()) }
    var rewardGiven by remember { mutableStateOf(false) }

    if (step == 4) {
        val totalKg = selections.values.sumOf { it.kgCO2.toDouble() }.toFloat()
        val avgKg = 7.0f // India average daily per capita CO₂ ~7 kg
        if (!rewardGiven) {
            rewardGiven = true
            gamViewModel.addXP(120)
            gamViewModel.addEcoPoints(120)
            gamViewModel.recordActivity()
        }
        CarbonResultScreen(totalKg, avgKg, selections, onBack = onBack, onReplay = {
            step = 0; selections = emptyMap(); rewardGiven = false
        })
        return
    }

    val cat = categories[step]
    val opts = optionsByCategory[cat.id] ?: emptyList()
    val selectedForCat = selections[cat.id]

    Column(Modifier.fillMaxSize().background(CloudWhite)) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFF1B5E20), Color(0xFF43A047))),
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (step == 0) onBack() else step-- },
                        modifier = Modifier.size(36.dp).background(Color.White.copy(0.15f), CircleShape)) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text("🌍 Carbon Footprint", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Step ${step + 1} of 4 — ${cat.label}", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                    Text(cat.emoji, fontSize = 28.sp)
                }
                Spacer(Modifier.height(12.dp))
                // Step indicator
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEachIndexed { i, c ->
                        Box(
                            Modifier.size(32.dp).clip(CircleShape)
                                .background(when {
                                    i < step -> Color(0xFF69F0AE)
                                    i == step -> Color.White
                                    else -> Color.White.copy(0.3f)
                                }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (i < step) "✓" else c.emoji, fontSize = 13.sp,
                                color = if (i == step) Color(0xFF1B5E20) else Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    val done = selections.values.sumOf { it.kgCO2.toDouble() }.toFloat()
                    if (done > 0) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(0.2f)) {
                            Text("So far: ${done.roundToInt()} kg CO₂", Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("${cat.emoji} ${cat.label}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DeepEarth)
            Text("Choose the option that best describes your daily habits:", fontSize = 13.sp, color = MistGray)
            Spacer(Modifier.height(16.dp))

            opts.forEach { opt ->
                val isSelected = selectedForCat == opt
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(if (isSelected) Color(0xFFE8F5E9) else Color.White),
                    elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 2.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                        .clickable { selections = selections + (cat.id to opt) }
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(40.dp).clip(CircleShape)
                                .background(if (isSelected) cat.color else Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            else Text(cat.emoji, fontSize = 18.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(opt.label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepEarth)
                            Text("${opt.kgCO2} kg CO₂/day", fontSize = 12.sp, color = MistGray)
                            if (isSelected) {
                                Spacer(Modifier.height(6.dp))
                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F8E9)) {
                                    Text("💡 ${opt.tip}", Modifier.padding(8.dp), fontSize = 11.sp, color = cat.color)
                                }
                            }
                        }
                        // CO2 bar
                        val maxKg = 5.5f
                        val frac = (opt.kgCO2 / maxKg).coerceIn(0f, 1f)
                        val barColor = when {
                            opt.kgCO2 <= 0.5f -> Color(0xFF4CAF50)
                            opt.kgCO2 <= 1.5f -> Color(0xFF8BC34A)
                            opt.kgCO2 <= 2.5f -> Color(0xFFFFC107)
                            opt.kgCO2 <= 3.5f -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                Modifier.width(10.dp).height(60.dp).clip(RoundedCornerShape(5.dp))
                                    .background(Color(0xFFF5F5F5))
                            ) {
                                Box(
                                    Modifier.width(10.dp).fillMaxHeight(frac).clip(RoundedCornerShape(5.dp))
                                        .background(barColor).align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { if (selectedForCat != null) step++ },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(if (selectedForCat != null) cat.color else MistGray),
                enabled = selectedForCat != null
            ) {
                Text(if (step < 3) "Next: ${categories[step + 1].label} →" else "See My Footprint 🌍",
                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CarbonResultScreen(
    totalKg: Float,
    avgKg: Float,
    selections: Map<String, CarbonOption>,
    onBack: () -> Unit,
    onReplay: () -> Unit
) {
    val isLow = totalKg < avgKg
    val percent = ((avgKg - totalKg) / avgKg * 100).roundToInt()

    Column(Modifier.fillMaxSize().background(CloudWhite).verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color(0xFF1B5E20), Color(0xFF66BB6A))),
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (isLow) "🌟" else "⚠️", fontSize = 56.sp)
                Spacer(Modifier.height(8.dp))
                Text("Your Daily Carbon Footprint", color = Color.White.copy(0.8f), fontSize = 14.sp)
                Text("${totalKg.roundToInt()} kg CO₂/day", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(0.2f)) {
                    Text(
                        if (isLow) "${percent}% BELOW average (${avgKg}kg)" else "${-percent}% ABOVE average (${avgKg}kg)",
                        Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("🌿 +120 XP  •  🌱 +120 Eco Points", color = Color.White, fontSize = 13.sp)
            }
        }

        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(16.dp))

            // Equivalents
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("📊 What ${totalKg.roundToInt()} kg CO₂ equals:", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DeepEarth)
                    Spacer(Modifier.height(12.dp))
                    listOf(
                        "🌳 ${(totalKg / 21.7f * 365).roundToInt()} trees needed/year to absorb this",
                        "🚗 ${(totalKg / 0.21f).roundToInt()} km driven in a petrol car",
                        "📱 ${(totalKg * 121).roundToInt()} smartphone charges",
                        "☕ ${(totalKg / 0.21f).roundToInt()} cups of coffee produced"
                    ).forEach { eq ->
                        Row(Modifier.padding(vertical = 4.dp)) {
                            Text(eq, fontSize = 13.sp, color = DeepEarth)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            // Breakdown
            Text("🗂️ Your Choices", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepEarth)
            Spacer(Modifier.height(10.dp))
            categories.forEach { cat ->
                val sel = selections[cat.id] ?: return@forEach
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(cat.emoji, fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(sel.label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DeepEarth)
                            Text("💡 ${sel.tip}", fontSize = 11.sp, color = MistGray)
                        }
                        Text("${sel.kgCO2}kg", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = cat.color)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp)) { Text("← Back") }
                Button(onClick = onReplay, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(Color(0xFF2E7D32))) { Text("🔄 Try Again") }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
