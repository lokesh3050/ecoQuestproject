package com.ecoquest.app.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.ui.theme.*

data class GameInfo(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val ecoPoints: Int,
    val difficulty: String,
    val gradient: List<Color>,
    val tag: String
)

private val games = listOf(
    GameInfo("waste", "♻️", "Waste Segregation", "Sort 12 items into the correct bins — wet, dry, hazardous & e-waste.", 80, 80, "Easy", listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)), "Drag & Drop"),
    GameInfo("water", "💧", "Water Conservation", "Manage daily water activities over 5 days and stay under 150 L/day.", 100, 100, "Medium", listOf(Color(0xFF0277BD), Color(0xFF29B6F6)), "Simulator"),
    GameInfo("carbon", "🌍", "Carbon Footprint", "Calculate & reduce your carbon footprint across travel, food & home.", 120, 120, "Medium", listOf(Color(0xFF1B5E20), Color(0xFF43A047)), "Calculator"),
    GameInfo("energy", "⚡", "Energy Management", "Optimise appliances in 4 rooms to stay within your 3 kWh/room budget.", 130, 130, "Hard", listOf(Color(0xFFE65100), Color(0xFFFFA726)), "Puzzle")
)

@Composable
fun GamesHubScreen(
    onNavigateToGame: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF1B5E20), EcoGreen)),
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack,
                        modifier = Modifier.size(36.dp).background(Color.White.copy(0.15f), CircleShape)) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("🎮 Mini-Games Hub", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Learn by playing • Earn XP & Eco Points", color = Color.White.copy(0.8f), fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatPill("🎮 ${games.size} Games", Modifier.weight(1f))
                    StatPill("⭐ 430 XP Total", Modifier.weight(1f))
                    StatPill("🌿 430 EP Total", Modifier.weight(1f))
                }
            }
        }

        Column(Modifier.padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Choose a Game", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
            Text("Complete games to earn XP and level up!", fontSize = 13.sp, color = MistGray)
            Spacer(Modifier.height(16.dp))

            games.forEach { game ->
                GameCard(game = game, onClick = { onNavigateToGame(game.id) })
                Spacer(Modifier.height(16.dp))
            }

            // Info card
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFE8F5E9)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("How it works", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EcoGreen)
                        Spacer(Modifier.height(4.dp))
                        Text("Play any game to earn XP & Eco Points. Your progress is saved automatically. Complete all 4 games to unlock the Eco Champion badge!", fontSize = 12.sp, color = Color(0xFF2E7D32))
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GameCard(game: GameInfo, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(Modifier.fillMaxWidth()) {
            // Gradient side panel
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(120.dp)
                    .background(Brush.verticalGradient(game.gradient), RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(game.emoji, fontSize = 36.sp)
                    Spacer(Modifier.height(4.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(0.25f)) {
                        Text(game.tag, Modifier.padding(horizontal = 6.dp, vertical = 3.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            // Content
            Column(Modifier.padding(16.dp).fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(game.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DeepEarth)
                    DifficultyBadge(game.difficulty)
                }
                Spacer(Modifier.height(6.dp))
                Text(game.description, fontSize = 12.sp, color = MistGray, lineHeight = 18.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RewardChip("⭐ ${game.xpReward} XP", EcoGreen)
                    RewardChip("🌿 ${game.ecoPoints} EP", OceanTeal)
                }
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty) {
        "Easy" -> Color(0xFF4CAF50)
        "Medium" -> Color(0xFFFFA726)
        "Hard" -> Color(0xFFEF5350)
        else -> MistGray
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(0.1f)) {
        Text(difficulty, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun RewardChip(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(12.dp), color = color.copy(0.1f)) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun StatPill(text: String, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(0.2f), modifier = modifier) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 6.dp).fillMaxWidth(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
    }
}
