package com.ecoquest.app.ui.screens

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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecoquest.app.data.model.ChallengeType
import com.ecoquest.app.data.model.DailyChallenge
import com.ecoquest.app.ui.components.SproutChatbot
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.ChatViewModel
import com.ecoquest.app.viewmodel.GamificationViewModel

@Composable
fun HomeScreen(
    user: Any?,
    chatViewModel: ChatViewModel,                          // ← shared VM passed in
    gamificationViewModel: GamificationViewModel = viewModel(),
    onNavigateToQuiz: (String, String) -> Unit,
    onNavigateToGames: () -> Unit
) {
    val userStats        by gamificationViewModel.userStats.collectAsState()
    val dailyChallenges  = gamificationViewModel.getDailyChallenges()
    var showQuizModal    by remember { mutableStateOf(false) }
    var selectedTopic    by remember { mutableStateOf("climate") }
    var activeChallenge  by remember { mutableStateOf<DailyChallenge?>(null) }
    var challengeInput   by remember { mutableStateOf("") }

    val levelProgress = (userStats.xp % 1000).toFloat() / 1000f
    val topics = listOf(
        "climate"      to "🌍 Climate",
        "waste"        to "♻️ Waste",
        "water"        to "💧 Water",
        "energy"       to "⚡ Energy"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Scrollable content ───────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(CloudWhite)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(EcoGreen, EcoGreenLight)),
                        RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(30.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("Good Day! 🌿", color = Color.White.copy(0.85f), fontSize = 13.sp)
                            Text("Eco Hero", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                StatPill("🔥 ${userStats.currentStreak}", Color(0xFFFF6F00))
                                StatPill("⭐ ${userStats.xp} XP", Color.White.copy(0.22f))
                                StatPill("🌱 ${userStats.ecoPoints} EP", Color.White.copy(0.22f))
                            }
                        }
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(0.1f), CircleShape)
                    ) {
                        Icon(Icons.Default.Notifications, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Column(Modifier.padding(16.dp)) {
                Spacer(Modifier.height(10.dp))

                // ── XP Progress Card ─────────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(userStats.levelName, color = EcoGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text("${userStats.xp} XP • ${userStats.ecoPoints} Eco Points", fontSize = 12.sp, color = MistGray)
                            }
                            Box(
                                Modifier
                                    .background(
                                        Brush.horizontalGradient(listOf(EcoGreen, SunYellow)),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Level ${userStats.level}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        LinearProgressIndicator(
                            progress = { levelProgress },
                            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                            color = EcoGreen,
                            trackColor = EcoGreenPale
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${(levelProgress * 100).toInt()}% to next level", fontSize = 11.sp, color = MistGray)
                            Text("${1000 - (userStats.xp % 1000)} XP to go", fontSize = 11.sp, color = MistGray)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Mini-Games CTA ───────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToGames() }
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF1B5E20), EcoGreenLight)),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(64.dp)
                                .background(Color.White.copy(0.15f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎮", fontSize = 36.sp)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(0.2f)) {
                                Text(
                                    "4 GAMES",
                                    Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 9.sp, fontWeight = FontWeight.Bold,
                                    color = Color.White, letterSpacing = 1.2.sp
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text("Interactive Mini-Games", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Drag & Drop • Earn XP • Learn by doing!", color = Color.White.copy(0.85f), fontSize = 12.sp)
                        }
                        Box(
                            Modifier
                                .size(44.dp)
                                .background(Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, null, tint = EcoGreen, modifier = Modifier.size(24.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Quick Launch Games ───────────────────────────────────────
                Text("🎯 Quick Launch", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GameTile("♻️", "Waste Sort",   listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)), Modifier.weight(1f)) { onNavigateToGames() }
                    GameTile("💧", "Water Sim",    listOf(Color(0xFF0277BD), Color(0xFF29B6F6)), Modifier.weight(1f)) { onNavigateToGames() }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GameTile("🌍", "Carbon Calc",  listOf(Color(0xFF1B5E20), Color(0xFF43A047)), Modifier.weight(1f)) { onNavigateToGames() }
                    GameTile("⚡", "Energy Puzzle",listOf(Color(0xFFE65100), Color(0xFFFFA726)), Modifier.weight(1f)) { onNavigateToGames() }
                }

                Spacer(Modifier.height(20.dp))

                // ── Daily Challenges ─────────────────────────────────────────
                Text("⚡ Daily Challenges", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                Spacer(Modifier.height(12.dp))
                if (dailyChallenges.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎉", fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("All challenges completed!", fontWeight = FontWeight.Bold, color = EcoGreen)
                                Text("Come back tomorrow for new challenges", fontSize = 12.sp, color = MistGray)
                            }
                        }
                    }
                } else {
                    dailyChallenges.forEach { challenge ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(
                                if (challenge.isCompleted) Color(0xFFF1F8E9) else Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable(enabled = !challenge.isCompleted) {
                                    activeChallenge = challenge
                                    challengeInput = ""
                                }
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (challenge.isCompleted) Icons.Default.CheckCircle else Icons.Default.Bolt,
                                    null,
                                    tint = if (challenge.isCompleted) EcoGreen else SunOrange
                                )
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        challenge.title,
                                        fontWeight = FontWeight.Bold,
                                        color = if (challenge.isCompleted) EcoGreen else DeepEarth
                                    )
                                    Text(challenge.description, fontSize = 12.sp, color = MistGray)
                                    Text(
                                        "+${challenge.xpReward} XP • +${challenge.pointsReward} EP",
                                        fontSize = 11.sp,
                                        color = EcoGreenLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                if (!challenge.isCompleted) {
                                    Icon(Icons.Default.ChevronRight, null, tint = MistGray)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Quick Quiz Banner ────────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth().clickable { showQuizModal = true }
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("🧠 QUICK QUIZ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OceanTeal, letterSpacing = 2.sp)
                            Text("Test Your Knowledge", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                            Text("Pick a topic and challenge yourself!", fontSize = 12.sp, color = MistGray)
                        }
                        Button(
                            onClick = { showQuizModal = true },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(OceanTeal)
                        ) {
                            Text("Play", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Stats Grid ───────────────────────────────────────────────
                Text("📊 Your Stats", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("🎮", "${userStats.totalGamesCompleted}", "Games Played", EcoGreen, Modifier.weight(1f))
                    StatCard("🔥", "${userStats.currentStreak}", "Day Streak",   SunOrange, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("⭐", "${userStats.xp}", "Total XP",    OceanTeal,     Modifier.weight(1f))
                    StatCard("🌿", "${userStats.ecoPoints}", "Eco Points", EcoGreenLight, Modifier.weight(1f))
                }

                Spacer(Modifier.height(20.dp))

                // ── Eco Tip ──────────────────────────────────────────────────
                Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFE3F2FD)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
                        Text("💡", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Eco Tip of the Day", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0277BD))
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Turning off lights for 1 hour saves enough energy to charge 24 smartphones. Small habits, big impact! 🌍",
                                fontSize = 12.sp, color = Color(0xFF0277BD), lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(100.dp)) // bottom padding for FAB
            }
        }

        // ── Sprout Chatbot FAB (shared ViewModel passed in) ──────────────────
        SproutChatbot(chatViewModel = chatViewModel)

        // ── Challenge Dialog ─────────────────────────────────────────────────
        activeChallenge?.let { ch ->
            Dialog(onDismissRequest = { activeChallenge = null }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Column(
                        Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(ch.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EcoGreen)
                        Spacer(Modifier.height(12.dp))
                        Text(ch.description, textAlign = TextAlign.Center, color = MistGray)
                        if (ch.type == ChallengeType.INPUT || ch.type == ChallengeType.QUIZ) {
                            Spacer(Modifier.height(16.dp))
                            ch.question?.let { q ->
                                Text(q, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                                Spacer(Modifier.height(8.dp))
                            }
                            OutlinedTextField(
                                value = challengeInput,
                                onValueChange = { challengeInput = it },
                                label = { Text("Your Answer") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { activeChallenge = null },
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("Cancel") }
                            Button(
                                onClick = {
                                    val ok = if (ch.correctAnswer != null)
                                        challengeInput.trim().equals(ch.correctAnswer, ignoreCase = true)
                                    else true
                                    if (ok) {
                                        gamificationViewModel.completeChallenge(ch)
                                        activeChallenge = null
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(EcoGreen)
                            ) { Text("Complete ✓") }
                        }
                    }
                }
            }
        }

        // ── Quiz Modal ────────────────────────────────────────────────────────
        if (showQuizModal) {
            Dialog(onDismissRequest = { showQuizModal = false }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧠 Quick Quiz", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                        Spacer(Modifier.height(16.dp))
                        Text("Select Topic", fontWeight = FontWeight.Bold, color = MistGray, modifier = Modifier.align(Alignment.Start))
                        Spacer(Modifier.height(8.dp))
                        topics.chunked(2).forEach { pair ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                pair.forEach { (id, label) ->
                                    val sel = selectedTopic == id
                                    OutlinedButton(
                                        onClick = { selectedTopic = id },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (sel) OceanTeal else Color(0xFFFAFAFA),
                                            contentColor   = if (sel) Color.White else MistGray
                                        )
                                    ) { Text(label, fontSize = 12.sp) }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Select Difficulty", fontWeight = FontWeight.Bold, color = MistGray, modifier = Modifier.align(Alignment.Start))
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("easy" to "🟢 Easy", "moderate" to "🟡 Med", "hard" to "🔴 Hard").forEach { (id, label) ->
                                Button(
                                    onClick = {
                                        onNavigateToQuiz(selectedTopic, id)
                                        showQuizModal = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(OceanTeal),
                                    contentPadding = PaddingValues(4.dp)
                                ) { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { showQuizModal = false }) { Text("Cancel") }
                    }
                }
            }
        }
    }
}

// ── Helper composables ────────────────────────────────────────────────────────

@Composable
private fun StatPill(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(20.dp), color = color) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GameTile(emoji: String, title: String, gradient: List<Color>, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(90.dp)
            .background(Brush.horizontalGradient(gradient), RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 30.sp)
            Spacer(Modifier.height(6.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun StatCard(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = MistGray)
        }
    }
}
