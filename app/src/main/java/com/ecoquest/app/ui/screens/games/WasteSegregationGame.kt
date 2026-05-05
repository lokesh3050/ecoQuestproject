package com.ecoquest.app.ui.screens.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecoquest.app.ui.theme.*
import com.ecoquest.app.viewmodel.GamificationViewModel
import kotlin.math.roundToInt

// ── Data models ─────────────────────────────────────────────────────────────

private data class WasteItemData(
    val id: Int,
    val emoji: String,
    val name: String,
    val correctBin: String
)

private data class BinData(
    val id: String,
    val emoji: String,
    val label: String,
    val color: Color,
    val bgColor: Color
)

private val ALL_ITEMS = listOf(
    WasteItemData(1,  "🍌", "Banana Peel",       "wet"),
    WasteItemData(2,  "📰", "Newspaper",          "dry"),
    WasteItemData(3,  "🔋", "Battery",            "hazardous"),
    WasteItemData(4,  "📱", "Old Phone",          "ewaste"),
    WasteItemData(5,  "🥑", "Avocado Skin",       "wet"),
    WasteItemData(6,  "🥤", "Plastic Bottle",     "dry"),
    WasteItemData(7,  "💊", "Medicine Pack",      "hazardous"),
    WasteItemData(8,  "💡", "CFL Bulb",           "ewaste"),
    WasteItemData(9,  "🌿", "Vegetable Waste",    "wet"),
    WasteItemData(10, "📦", "Cardboard Box",      "dry"),
    WasteItemData(11, "🎨", "Paint Can",          "hazardous"),
    WasteItemData(12, "🖥️", "Old Monitor",       "ewaste")
)

private val BINS = listOf(
    BinData("wet",       "🟢", "Wet Waste",      Color(0xFF2E7D32), Color(0xFFE8F5E9)),
    BinData("dry",       "🔵", "Dry Waste",       Color(0xFF1565C0), Color(0xFFE3F2FD)),
    BinData("hazardous", "🔴", "Hazardous",       Color(0xFFC62828), Color(0xFFFFEBEE)),
    BinData("ewaste",    "🟡", "E-Waste",         Color(0xFFE65100), Color(0xFFFFF3E0))
)

// ── Main composable ──────────────────────────────────────────────────────────

@Composable
fun WasteSegregationGame(
    onBack: () -> Unit,
    gamViewModel: GamificationViewModel = viewModel()
) {
    // Item dragging state
    var draggingItemId by remember { mutableStateOf<Int?>(null) }
    var dragOffset    by remember { mutableStateOf(Offset.Zero) }
    var dragStart     by remember { mutableStateOf(Offset.Zero) }

    // Game state
    var sortedMap     by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var feedback      by remember { mutableStateOf<Triple<String, Boolean, String>?>(null) } // msg, correct, binId
    var gameOver      by remember { mutableStateOf(false) }
    var rewardGiven   by remember { mutableStateOf(false) }

    // Bin bounds for hit-testing
    val binBounds = remember { mutableStateMapOf<String, Rect>() }

    val score = sortedMap.entries.count { (id, bin) -> ALL_ITEMS.find { it.id == id }?.correctBin == bin }

    if (sortedMap.size == ALL_ITEMS.size && !gameOver) {
        gameOver = true
        if (!rewardGiven) {
            rewardGiven = true
            gamViewModel.addXP(80)
            gamViewModel.addEcoPoints(80)
            gamViewModel.recordActivity()
        }
    }

    if (gameOver) {
        GameResultScreen(
            emoji = "♻️",
            title = "Waste Sorted!",
            score = score,
            total = ALL_ITEMS.size,
            xpEarned = 80,
            epEarned = 80,
            gradientColors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)),
            onBack = onBack,
            onReplay = {
                sortedMap = emptyMap()
                draggingItemId = null
                dragOffset = Offset.Zero
                gameOver = false
                rewardGiven = false
                feedback = null
            }
        )
        return
    }

    val unsorted = ALL_ITEMS.filter { it.id !in sortedMap.keys }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CloudWhite)
        ) {
            // ── Header ───────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF1B5E20), Color(0xFF66BB6A))),
                        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(0.18f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "♻️ Waste Segregation",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Drag items into the correct bins!",
                            color = Color.White.copy(0.8f),
                            fontSize = 12.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(0.2f)
                    ) {
                        Text(
                            "$score/${ALL_ITEMS.size}",
                            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                // ── Instruction banner ───────────────────────────────────────
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👆", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Long-press & drag items onto the correct bin below.",
                            fontSize = 13.sp,
                            color = Color(0xFF2E7D32),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── Feedback banner ──────────────────────────────────────────
                feedback?.let { (msg, correct, _) ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (correct) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (correct) "✅" else "❌", fontSize = 20.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                msg,
                                fontSize = 13.sp,
                                color = if (correct) Color(0xFF1B5E20) else Color(0xFFB71C1C),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                // ── Bin drop zones ───────────────────────────────────────────
                Text(
                    "🗑️ Drop Zones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DeepEarth
                )
                Spacer(Modifier.height(8.dp))

                // 2×2 bin grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BINS.chunked(2).forEach { row ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { bin ->
                                val isHovered = draggingItemId != null &&
                                        binBounds[bin.id]?.let { bounds ->
                                            bounds.contains(dragOffset)
                                        } == true

                                val hoverScale by animateFloatAsState(
                                    if (isHovered) 1.06f else 1f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                    label = "hover_scale_${bin.id}"
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(90.dp)
                                        .scale(hoverScale)
                                        .shadow(if (isHovered) 8.dp else 2.dp, RoundedCornerShape(18.dp))
                                        .background(
                                            color = if (isHovered) bin.color.copy(0.28f) else bin.bgColor,
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .border(
                                            width = if (isHovered) 2.dp else 1.dp,
                                            color = if (isHovered) bin.color else bin.color.copy(0.3f),
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .onGloballyPositioned { coords ->
                                            binBounds[bin.id] = coords.boundsInWindow()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(bin.emoji, fontSize = 26.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            bin.label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = bin.color,
                                            textAlign = TextAlign.Center
                                        )
                                        if (isHovered) {
                                            Text(
                                                "DROP HERE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = bin.color.copy(0.7f),
                                                letterSpacing = 1.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // ── Items to sort ────────────────────────────────────────────
                if (unsorted.isNotEmpty()) {
                    Text(
                        "📦 Items to Sort  (${unsorted.size} left)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DeepEarth
                    )
                    Spacer(Modifier.height(10.dp))

                    // 3-column grid using fixed rows
                    val rows = unsorted.chunked(3)
                    rows.forEach { rowItems ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { item ->
                                val isDragging = draggingItemId == item.id
                                val cardScale by animateFloatAsState(
                                    if (isDragging) 1.12f else 1f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                    label = "card_scale_${item.id}"
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(90.dp)
                                        .scale(cardScale)
                                        .zIndex(if (isDragging) 10f else 0f)
                                        .shadow(if (isDragging) 12.dp else 3.dp, RoundedCornerShape(16.dp))
                                        .background(
                                            color = if (isDragging) Color(0xFFFFF9C4) else Color.White,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .border(
                                            width = if (isDragging) 2.dp else 0.dp,
                                            color = if (isDragging) Color(0xFFFDD835) else Color.Transparent,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .onGloballyPositioned { coords ->
                                            if (isDragging) {
                                                val center = coords.boundsInWindow().center
                                                dragOffset = center + dragOffset - dragStart
                                            }
                                        }
                                        .pointerInput(item.id) {
                                            detectDragGestures(
                                                onDragStart = { localOffset ->
                                                    draggingItemId = item.id
                                                    dragStart = localOffset
                                                    dragOffset = Offset.Zero
                                                    feedback = null
                                                },
                                                onDrag = { change, delta ->
                                                    change.consume()
                                                    dragOffset += delta
                                                },
                                                onDragEnd = {
                                                    // Hit-test: find which bin contains current touch position
                                                    val dropTarget = binBounds.entries.firstOrNull { (_, bounds) ->
                                                        bounds.contains(dragOffset)
                                                    }?.key

                                                    if (dropTarget != null) {
                                                        val correct = item.correctBin == dropTarget
                                                        val binName = BINS.find { it.id == dropTarget }?.label ?: dropTarget
                                                        sortedMap = sortedMap + (item.id to dropTarget)
                                                        feedback = Triple(
                                                            if (correct)
                                                                "${item.name} ✓ Correct — goes in $binName!"
                                                            else
                                                                "${item.name} ✗ Should go in ${BINS.find { it.id == item.correctBin }?.label}!",
                                                            correct,
                                                            dropTarget
                                                        )
                                                    }
                                                    draggingItemId = null
                                                    dragOffset = Offset.Zero
                                                    dragStart = Offset.Zero
                                                },
                                                onDragCancel = {
                                                    draggingItemId = null
                                                    dragOffset = Offset.Zero
                                                    dragStart = Offset.Zero
                                                }
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(6.dp)
                                    ) {
                                        Text(item.emoji, fontSize = 30.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            item.name,
                                            fontSize = 9.sp,
                                            textAlign = TextAlign.Center,
                                            color = DeepEarth,
                                            fontWeight = FontWeight.SemiBold,
                                            lineHeight = 13.sp
                                        )
                                    }
                                }
                            }
                            // fill remaining slots in partial rows
                            repeat(3 - rowItems.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }

                // ── Already sorted strip ─────────────────────────────────────
                val sorted = ALL_ITEMS.filter { it.id in sortedMap.keys }
                if (sorted.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF1F8E9)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "✅ Sorted (${sorted.size}/${ALL_ITEMS.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = EcoGreen
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                sorted.forEach { item ->
                                    val isCorrect = item.correctBin == sortedMap[item.id]
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isCorrect) Color(0xFFDCEDC8) else Color(0xFFFFCDD2)
                                    ) {
                                        Column(
                                            Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(item.emoji, fontSize = 16.sp)
                                            Text(
                                                if (isCorrect) "✓" else "✗",
                                                fontSize = 10.sp,
                                                color = if (isCorrect) EcoGreen else Color(0xFFC62828),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        // ── Floating drag ghost ───────────────────────────────────────────────
        draggingItemId?.let { dragId ->
            val item = ALL_ITEMS.find { it.id == dragId } ?: return@let
            Box(
                modifier = Modifier
                    .offset { IntOffset(dragOffset.x.roundToInt() - 40, dragOffset.y.roundToInt() - 40) }
                    .size(80.dp)
                    .zIndex(100f)
                    .shadow(16.dp, RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF9C4), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFFDD835), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(item.emoji, fontSize = 32.sp)
                    Text(
                        item.name,
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                        color = DeepEarth,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 11.sp
                    )
                }
            }
        }
    }
}
