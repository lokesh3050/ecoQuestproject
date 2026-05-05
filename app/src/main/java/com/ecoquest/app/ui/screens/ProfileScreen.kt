package com.ecoquest.app.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import coil.compose.rememberAsyncImagePainter
import com.ecoquest.app.R
import com.ecoquest.app.ui.theme.*

data class Badge(
    val icon: String,
    val name: String,
    val earned: Boolean
)

data class ProfileStat(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: Any?, // Mock user
    onSignOut: () -> Unit,
    onViewCertificate: () -> Unit
) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    val badges = remember {
        listOf(
            Badge("🌱", "First Leaf", true),
            Badge("🔥", "On Fire", true),
            Badge("♻️", "Recycler", true),
            Badge("🏆", "Quiz Master", false),
            Badge("🌊", "Ocean Saver", false),
            Badge("🌍", "Earth War", false),
        )
    }

    val stats = remember {
        listOf(
            ProfileStat("Total XP", "750", Icons.Filled.EmojiEvents, SunYellow),
            ProfileStat("Courses", "3", Icons.Filled.MenuBook, OceanTeal),
            ProfileStat("Quizzes", "12", Icons.Filled.CheckCircle, EcoGreen),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Cover Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/seed/nature3/800/400"),
                contentDescription = "Cover",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            )

            // Top-right buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { showLanguageDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Filled.Language,
                        contentDescription = "Language",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onSignOut,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Profile card overlapping banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-48).dp)
                .padding(horizontal = 20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .offset(y = (-64).dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = EcoGreen, modifier = Modifier.size(48.dp).align(Alignment.Center))
                    }

                    // Push content up after the offset avatar
                    Spacer(modifier = Modifier.height((-48).dp))

                    Text(
                        text = "Eco Student",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepEarth
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Level 4 • Tree",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = EcoGreen
                    )

                    Text(
                        text = "Delhi Public School • Class 10",
                        fontSize = 12.sp,
                        color = SoilBrown.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EcoGreenPale,
                                contentColor = EcoGreen
                            )
                        ) {
                            Text(stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        
                        Button(
                            onClick = onViewCertificate,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OceanTeal,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.CardMembership, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Certificate", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stats.forEach { stat ->
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                stat.icon,
                                contentDescription = null,
                                tint = stat.color,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stat.value,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepEarth
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stat.label.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoilBrown.copy(alpha = 0.6f),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Badges
            Text(
                text = "Badges",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepEarth
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Badges grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in badges.indices step 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        badges.subList(i, minOf(i + 4, badges.size)).forEach { badge ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (badge.earned) Color.White else Color(0xFFFAFAFA)
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = if (badge.earned) 1.dp else 0.dp
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = badge.icon,
                                        fontSize = 24.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = badge.name,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        color = if (badge.earned) DeepEarth else MistGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Select Language") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("English") },
                        modifier = Modifier.clickable {
                            setLocale("en")
                            showLanguageDialog = false
                        }
                    )
                    ListItem(
                        headlineContent = { Text("हिंदी (Hindi)") },
                        modifier = Modifier.clickable {
                            setLocale("hi")
                            showLanguageDialog = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

fun setLocale(langCode: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
