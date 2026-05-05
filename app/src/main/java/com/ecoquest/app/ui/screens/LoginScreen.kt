package com.ecoquest.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onAdminLogin: (String, String) -> Unit,
    onSignedIn: () -> Unit,
    authState: AuthState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navigate away if signed in
    LaunchedEffect(authState.user, authState.isTestUser) {
        if (authState.user != null || authState.isTestUser) {
            onSignedIn()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepEarth)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://picsum.photos/seed/forest/800/1200"),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Eco,
                        contentDescription = "EcoQuest",
                        modifier = Modifier.size(48.dp),
                        tint = EcoGreenLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "EcoQuest",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = CloudWhite) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = EcoGreenLight) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = EcoGreenLight,
                            unfocusedBorderColor = CloudWhite.copy(alpha = 0.5f),
                            cursorColor = EcoGreenLight
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = CloudWhite) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = EcoGreenLight) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = EcoGreenLight,
                            unfocusedBorderColor = CloudWhite.copy(alpha = 0.5f),
                            cursorColor = EcoGreenLight
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Test Login Button
                    Button(
                        onClick = { onAdminLogin(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
                    ) {
                        Text("Login (Admin Test)", fontWeight = FontWeight.Bold)
                    }

                    authState.error?.let { error ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error,
                            color = Color(0xFFEF5350),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
