package com.ecoquest.app.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun GameResultScreen(
    emoji: String,
    title: String,
    score: Int,
    total: Int,
    xpEarned: Int,
    epEarned: Int,
    gradientColors: List<Color>,
    onBack: () -> Unit,
    onReplay: () -> Unit
) {
    val percentage = if (total > 0) (score * 100) / total else 0
    val (grade, gradeColor, msg) = when {
        percentage >= 90 -> Triple("S", Color(0xFFFFD700), "Outstanding! You're an Eco Champion! 🏆")
        percentage >= 75 -> Triple("A", Color(0xFF4CAF50), "Excellent work! Nature thanks you! 🌿")
        percentage >= 60 -> Triple("B", Color(0xFF29B6F6), "Good job! Keep learning and improving! 💧")
        percentage >= 40 -> Triple("C", Color(0xFFFFA726), "Nice try! Practice makes perfect! 🌱")
        else             -> Triple("D", Color(0xFFEF5350), "Keep trying — you'll get there! 💪")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(gradientColors),
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(horizontal = 24.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(emoji, fontSize = 64.sp)
                Spacer(Modifier.height(12.dp))
                Text(title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                // Grade circle
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(0.2f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(grade, fontSize = 48.sp, fontWeight = FontWeight.Black, color = gradeColor)
                        Text("Grade", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("$score / $total  ($percentage%)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(msg, color = Color.White.copy(0.9f), fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }

        Column(Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            // Rewards
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("🎁 Rewards Earned", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DeepEarth)
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        RewardBox("⭐", "$xpEarned XP", "Experience Points", EcoGreen, Modifier.weight(1f))
                        RewardBox("🌱", "$epEarned EP", "Eco Points", OceanTeal, Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tips / motivation card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF3E5F5)
            ) {
                Row(Modifier.padding(16.dp)) {
                    Text("🌍", fontSize = 30.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Keep Going!", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF6A1B9A))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Every eco action you take adds up. Play more games to earn badges and climb the leaderboard!",
                            fontSize = 12.sp, color = Color(0xFF6A1B9A).copy(0.8f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("← Games Hub", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onReplay,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(gradientColors.first())
                ) {
                    Text("🔄 Play Again", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RewardBox(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(0.1f),
        modifier = modifier
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = color)
            Text(label, fontSize = 11.sp, color = MistGray, textAlign = TextAlign.Center)
        }
    }
}
