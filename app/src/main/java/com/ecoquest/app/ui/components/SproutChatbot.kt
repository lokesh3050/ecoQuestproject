package com.ecoquest.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.ChatMessage
import com.ecoquest.app.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

/**
 * SproutChatbot — floating AI chat widget.
 * IMPORTANT: chatViewModel must be passed in from the parent composable (created via viewModel()
 * at the NavHost/Activity level) so the same instance is shared across recompositions.
 */
@Composable
fun SproutChatbot(chatViewModel: ChatViewModel) {
    var isOpen by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val chatState by chatViewModel.chatState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll to latest message
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

    val suggestions = remember {
        listOf(
            "♻️ How to segregate waste?",
            "💧 Water saving tips",
            "🌍 What is carbon footprint?",
            "⚡ Renewable energy types"
        )
    }

    fun sendMessage() {
        val msg = inputText.trim()
        if (msg.isNotBlank() && !chatState.isLoading) {
            chatViewModel.sendMessage(msg)
            inputText = ""
            scope.launch {
                listState.animateScrollToItem(maxOf(0, chatState.messages.size))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {

        // ── FAB (closed state) ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible = !isOpen,
            enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit  = scaleOut() + fadeOut(),
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = EcoGreen,
                    shadowElevation = 6.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        "Ask Sprout 🌱",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                FloatingActionButton(
                    onClick = { isOpen = true },
                    modifier = Modifier
                        .size(62.dp)
                        .shadow(14.dp, CircleShape),
                    shape = CircleShape,
                    containerColor = EcoGreen,
                    contentColor = Color.White
                ) {
                    Text("🌱", fontSize = 28.sp)
                }
            }
        }

        // ── Chat Panel (open state) ────────────────────────────────────────────
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + scaleIn(
                initialScale = 0.85f,
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(1f, 1f)
            ) + fadeIn(),
            exit  = slideOutVertically(targetOffsetY = { it / 2 }) + scaleOut(
                targetScale = 0.85f,
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(1f, 1f)
            ) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(530.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // ── Header ─────────────────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFF1B5E20), EcoGreenLight))
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.25f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🌱", fontSize = 22.sp)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Sprout AI",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .background(Color(0xFF69F0AE), CircleShape)
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        "EcoQuest Assistant • Online",
                                        color = Color.White.copy(0.85f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            // Clear chat button
                            IconButton(
                                onClick = { chatViewModel.clearChat() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = "Clear chat",
                                    tint = Color.White.copy(0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { isOpen = false },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // ── Messages ───────────────────────────────────────────────
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = chatState.messages,
                            key = { msg -> msg.hashCode() + chatState.messages.indexOf(msg) }
                        ) { msg ->
                            ChatBubble(msg)
                        }
                        if (chatState.isLoading) {
                            item { TypingIndicator() }
                        }
                    }

                    // ── Quick suggestions (shown only at start) ────────────────
                    AnimatedVisibility(visible = chatState.messages.size <= 1 && !chatState.isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Try asking:",
                                fontSize = 11.sp,
                                color = MistGray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            suggestions.chunked(2).forEach { row ->
                                Row(
                                    Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    row.forEach { suggestion ->
                                        AssistChip(
                                            onClick = {
                                                chatViewModel.sendMessage(suggestion)
                                            },
                                            label = {
                                                Text(
                                                    suggestion,
                                                    fontSize = 10.sp,
                                                    maxLines = 1
                                                )
                                            },
                                            colors = AssistChipDefaults.assistChipColors(
                                                containerColor = EcoGreenPale,
                                                labelColor = EcoGreen
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── Input bar ──────────────────────────────────────────────
                    HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .navigationBarsPadding()
                            .imePadding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    "Ask Sprout anything eco…",
                                    fontSize = 13.sp,
                                    color = MistGray
                                )
                            },
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { sendMessage() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EcoGreen,
                                unfocusedBorderColor = Color(0xFFDDDDDD),
                                focusedContainerColor = Color(0xFFF5F5F5),
                                unfocusedContainerColor = Color(0xFFF5F5F5)
                            ),
                            enabled = !chatState.isLoading
                        )
                        Spacer(Modifier.width(8.dp))
                        // Send button
                        Surface(
                            shape = CircleShape,
                            color = if (inputText.isNotBlank() && !chatState.isLoading)
                                EcoGreen
                            else
                                Color(0xFFDDDDDD),
                            modifier = Modifier.size(46.dp),
                            onClick = { sendMessage() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (chatState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Filled.Send,
                                        contentDescription = "Send",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
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

// ── Chat bubble ─────────────────────────────────────────────────────────────

@Composable
fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!msg.isUser) {
            Surface(
                shape = CircleShape,
                color = EcoGreenPale,
                modifier = Modifier.size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🌱", fontSize = 15.sp)
                }
            }
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp, topEnd = 18.dp,
                bottomStart = if (msg.isUser) 18.dp else 4.dp,
                bottomEnd   = if (msg.isUser) 4.dp else 18.dp
            ),
            color = if (msg.isUser) EcoGreen else Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 270.dp)
        ) {
            Text(
                text     = msg.text,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                color    = if (msg.isUser) Color.White else Color(0xFF1A1A1A),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }

        if (msg.isUser) {
            Spacer(Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = EcoGreen,
                modifier = Modifier.size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ── Typing indicator ─────────────────────────────────────────────────────────

@Composable
fun TypingIndicator() {
    Row(verticalAlignment = Alignment.Bottom) {
        Surface(shape = CircleShape, color = EcoGreenPale, modifier = Modifier.size(30.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text("🌱", fontSize = 15.sp)
            }
        }
        Spacer(Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { i ->
                    val inf = rememberInfiniteTransition(label = "dot$i")
                    val offsetY by inf.animateFloat(
                        initialValue = 0f, targetValue = -6f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, delayMillis = i * 160, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "bounce$i"
                    )
                    Box(
                        modifier = Modifier
                            .offset(y = offsetY.dp)
                            .size(8.dp)
                            .background(EcoGreen, CircleShape)
                    )
                }
            }
        }
    }
}
