package com.ecoquest.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ecoquest.app.ui.theme.*

data class CourseItem(
    val title: String,
    val time: String,
    val diff: String,
    val xp: Int,
    val img: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen() {
    val courses = remember {
        listOf(
            CourseItem("Climate Change: Basics", "40 min", "Beginner", 150, "climate"),
            CourseItem("Intro to Artificial Intelligence", "1 hr", "Beginner", 200, "network"),
            CourseItem("Web Development Bootcamp", "2 hr", "Intermediate", 300, "code"),
            CourseItem("Cloud Computing Fundamentals", "55 min", "Intermediate", 200, "server"),
            CourseItem("Cybersecurity Essentials", "1 hr 10 min", "Advanced", 300, "lock"),
            CourseItem("Renewable Energy", "30 min", "Beginner", 100, "solar"),
        )
    }

    val categories = remember { listOf("All", "AI", "Cybersecurity", "Web Dev", "Cloud", "Climate") }
    var selectedCategory by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Courses",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepEarth
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, RoundedCornerShape(50))
            ) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = SoilBrown)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text("Search for topics...", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = MistGray)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = EcoGreenLight
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(categories) { index, category ->
                FilterChip(
                    selected = selectedCategory == index,
                    onClick = { selectedCategory = index },
                    label = {
                        Text(
                            category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EcoGreen,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = SoilBrown.copy(alpha = 0.7f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedCategory == index,
                        borderColor = Color(0xFFE0E0E0),
                        selectedBorderColor = EcoGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Course list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses) { course ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://picsum.photos/seed/${course.img}/200/200"),
                            contentDescription = course.title,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = course.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DeepEarth,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = SoilBrown.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = course.time,
                                    fontSize = 12.sp,
                                    color = SoilBrown.copy(alpha = 0.6f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val diffColor = when (course.diff) {
                                    "Beginner" -> Color(0xFF2E7D32) to Color(0xFFE8F5E9)
                                    "Intermediate" -> Color(0xFFE65100) to Color(0xFFFFF3E0)
                                    else -> Color(0xFFC62828) to Color(0xFFFFEBEE)
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = diffColor.second
                                ) {
                                    Text(
                                        course.diff,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = diffColor.first
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(EcoGreen, SunYellow)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Text(
                                        "+${course.xp} XP",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
