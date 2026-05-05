package com.ecoquest.app.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecoquest.app.data.QuizData
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.GamificationViewModel
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    topic: String,
    difficulty: String,
    onNavigateHome: () -> Unit,
    onBack: () -> Unit,
    gamificationViewModel: GamificationViewModel = viewModel()
) {
    val context = LocalContext.current
    val questions = remember { QuizData.getQuestions(topic, difficulty) }

    var currentIndex by remember { mutableIntStateOf(0) }
    var correct by remember { mutableIntStateOf(0) }
    var wrong by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isFinished by remember { mutableStateOf(false) }

    val currentQ = questions[currentIndex]

    fun handleSelect(idx: Int) {
        if (selectedOption != null) return
        selectedOption = idx
        if (idx == currentQ.correctIndex) {
            correct++
        } else {
            wrong++
        }
    }

    fun handleNext() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            selectedOption = null
        } else {
            isFinished = true
            // Award points and XP for finishing
            val pointsEarned = correct * 10
            val xpEarned = correct * 20
            gamificationViewModel.addEcoPoints(pointsEarned)
            gamificationViewModel.addXP(xpEarned)
            gamificationViewModel.recordActivity()
        }
    }

    // ===== FINISHED STATE =====
    if (isFinished) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CloudWhite)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.VerifiedUser,
                        contentDescription = null,
                        tint = OceanTeal,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Quiz Complete!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepEarth
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You've finished the $difficulty challenge.",
                        fontSize = 14.sp,
                        color = SoilBrown.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(48.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$correct",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen
                            )
                            Text(
                                text = "CORRECT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MistGray,
                                letterSpacing = 1.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$wrong",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF5350)
                            )
                            Text(
                                text = "WRONG",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MistGray,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "Earned: ${correct * 10} EP & ${correct * 20} XP",
                        fontWeight = FontWeight.Bold,
                        color = OceanTeal,
                        fontSize = 18.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onNavigateHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanTeal),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            "Back to Home",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        return
    }

    // ===== ACTIVE QUIZ STATE =====
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
    ) {
        // Top Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MistGray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "${difficulty.replaceFirstChar { it.uppercase() }} Quiz",
                    fontWeight = FontWeight.Bold,
                    color = DeepEarth
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = EcoGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "$correct",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = EcoGreen
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = null,
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "$wrong",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFEF5350)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(24.dp)
        ) {
            // Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "QUESTION ${currentIndex + 1}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoilBrown.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "OF ${questions.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoilBrown.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = OceanTeal,
                trackColor = Color(0xFFE0E0E0),
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Question text
            Text(
                text = currentQ.text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepEarth,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Options
            currentQ.options.forEachIndexed { idx, option ->
                val isSelected = selectedOption == idx
                val isCorrect = idx == currentQ.correctIndex
                val hasAnswered = selectedOption != null

                val bgColor = when {
                    hasAnswered && isCorrect -> Color(0xFFF1F8E9)
                    hasAnswered && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                    hasAnswered -> Color.White.copy(alpha = 0.5f)
                    else -> Color.White
                }

                val borderColor = when {
                    hasAnswered && isCorrect -> Color(0xFF4CAF50)
                    hasAnswered && isSelected && !isCorrect -> Color(0xFFEF5350)
                    else -> Color(0xFFE0E0E0)
                }

                val textColor = when {
                    hasAnswered && isCorrect -> Color(0xFF1B5E20)
                    hasAnswered && isSelected && !isCorrect -> Color(0xFFB71C1C)
                    hasAnswered -> DeepEarth.copy(alpha = 0.5f)
                    else -> DeepEarth
                }

                OutlinedButton(
                    onClick = { handleSelect(idx) },
                    enabled = !hasAnswered,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = bgColor,
                        contentColor = textColor,
                        disabledContainerColor = bgColor,
                        disabledContentColor = textColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        width = 2.dp,
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(borderColor, borderColor)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (hasAnswered && isCorrect) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        if (hasAnswered && isSelected && !isCorrect) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = null,
                                tint = Color(0xFFEF5350),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Next button
            AnimatedVisibility(
                visible = selectedOption != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut()
            ) {
                Button(
                    onClick = { handleNext() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanTeal),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = if (currentIndex < questions.size - 1) "Next Question" else "Finish Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (currentIndex < questions.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
