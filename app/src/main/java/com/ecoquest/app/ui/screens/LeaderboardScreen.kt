package com.ecoquest.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ecoquest.app.ui.theme.*

data class LeaderUser(
    val rank: Int,
    val name: String,
    val xp: Int,
    val change: String,
    val avatar: String,
    val isMe: Boolean = false
)

@Composable
fun LeaderboardScreen() {
    val leaders = remember {
        listOf(
            LeaderUser(1, "Priya S.", 1250, "up", "face1"),
            LeaderUser(2, "Rohan D.", 1120, "down", "face2"),
            LeaderUser(3, "Ananya M.", 980, "same", "face3"),
            LeaderUser(4, "Aditya V.", 850, "up", "face4"),
            LeaderUser(5, "Neha K.", 620, "up", "face5"),
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
    ) {
        // Teal header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    OceanTeal,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
                .padding(top = 40.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Leaderboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Weekly / All Time toggle
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Button(
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 0) Color.White else Color.Transparent,
                                contentColor = if (selectedTab == 0) OceanTeal else Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (selectedTab == 0) 2.dp else 0.dp
                            )
                        ) {
                            Text(
                                "Weekly",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Button(
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 1) Color.White else Color.Transparent,
                                contentColor = if (selectedTab == 1) OceanTeal else Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (selectedTab == 1) 2.dp else 0.dp
                            )
                        ) {
                            Text(
                                "All Time",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Leaders list (overlapping the header)
        Column(
            modifier = Modifier
                .offset(y = (-56).dp)
                .padding(horizontal = 16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    leaders.forEach { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank
                            Text(
                                text = "#${user.rank}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = when (user.rank) {
                                    1 -> SunYellow
                                    2 -> MistGray
                                    3 -> Color(0xFF8D6E63)
                                    else -> SoilBrown.copy(alpha = 0.4f)
                                },
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Avatar
                            Image(
                                painter = rememberAsyncImagePainter("https://picsum.photos/seed/${user.avatar}/100/100"),
                                contentDescription = user.name,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Name
                            Text(
                                text = user.name,
                                fontWeight = FontWeight.Bold,
                                color = DeepEarth,
                                modifier = Modifier.weight(1f)
                            )

                            // XP and trend
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${user.xp} XP",
                                    fontWeight = FontWeight.Bold,
                                    color = EcoGreen
                                )
                                when (user.change) {
                                    "up" -> Icon(
                                        Icons.Filled.TrendingUp,
                                        contentDescription = "Trending up",
                                        tint = EcoGreenLight,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    "down" -> Icon(
                                        Icons.Filled.TrendingDown,
                                        contentDescription = "Trending down",
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        if (user.rank < 5) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                color = Color(0xFFF5F5F5)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current user card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EcoGreenPale),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#24",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = EcoGreen,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = rememberAsyncImagePainter("https://picsum.photos/seed/me/100/100"),
                        contentDescription = "You",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "You",
                        fontWeight = FontWeight.Bold,
                        color = EcoGreen,
                        modifier = Modifier.weight(1f)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "340 XP",
                            fontWeight = FontWeight.Bold,
                            color = EcoGreen
                        )
                        Icon(
                            Icons.Filled.TrendingUp,
                            contentDescription = "Trending up",
                            tint = EcoGreenLight,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
