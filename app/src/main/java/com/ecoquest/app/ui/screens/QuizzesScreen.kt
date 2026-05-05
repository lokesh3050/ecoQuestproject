package com.ecoquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.ui.theme.*

data class QuizCategory(
    val id: String,
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val color: Color,
    val bg: Color,
    val border: Color
)

data class DifficultyItem(
    val id: String,
    val label: String,
    val textColor: Color,
    val bgColor: Color
)

@Composable
fun QuizzesScreen(
    onNavigateToQuiz: (String, String) -> Unit
) {
    val quizCategories = remember {
        listOf(
            QuizCategory(
                "cybersecurity", "Cybersecurity",
                "Test your knowledge on digital security, threats, and defense mechanisms.",
                Icons.Filled.Shield, Color(0xFF7B1FA2), Color(0xFFF3E5F5), Color(0xFFCE93D8)
            ),
            QuizCategory(
                "ai", "Artificial Intelligence",
                "Explore AI concepts, machine learning, and neural networks.",
                Icons.Filled.Memory, Color(0xFF1565C0), Color(0xFFE3F2FD), Color(0xFF90CAF9)
            ),
            QuizCategory(
                "webdev", "Web Development",
                "Assess your front-end and back-end web development skills.",
                Icons.Filled.Code, Color(0xFFE65100), Color(0xFFFFF3E0), Color(0xFFFFCC80)
            ),
            QuizCategory(
                "cloud", "Cloud Computing",
                "Evaluate your understanding of cloud architecture and services.",
                Icons.Filled.Cloud, Color(0xFF0277BD), Color(0xFFE1F5FE), Color(0xFF81D4FA)
            )
        )
    }

    val difficulties = remember {
        listOf(
            DifficultyItem("easy", "Easy", Color(0xFF33691E), Color(0xFFF1F8E9)),
            DifficultyItem("moderate", "Moderate", Color(0xFFF57F17), Color(0xFFFFF8E1)),
            DifficultyItem("hard", "Hard", Color(0xFFC62828), Color(0xFFFFEBEE))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Psychology,
                contentDescription = null,
                tint = OceanTeal,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Knowledge Quizzes",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepEarth
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Challenge yourself! Each category contains dedicated quizzes categorized by difficulty. Select a level to begin.",
            fontSize = 14.sp,
            color = SoilBrown.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Category Cards
        quizCategories.forEach { cat ->
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = cat.bg,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = cat.border
                            )
                        ) {
                            Icon(
                                cat.icon,
                                contentDescription = null,
                                tint = cat.color,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = cat.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepEarth
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = cat.desc,
                                fontSize = 12.sp,
                                color = SoilBrown.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Difficulty buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        difficulties.forEach { item ->
                            Button(
                                onClick = { onNavigateToQuiz(cat.id, item.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = item.bgColor,
                                    contentColor = item.textColor
                                ),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = item.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "10 Qs",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(
                                            Icons.Filled.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
