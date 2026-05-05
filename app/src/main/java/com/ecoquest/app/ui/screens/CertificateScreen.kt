package com.ecoquest.app.ui.screens

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.ui.theme.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CertificateScreen(
    studentName: String,
    schoolName: String,
    achievement: String,
    date: String = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your Achievement!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DeepEarth
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Certificate Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.414f), // A4 ratio
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background Design
                ComposeCanvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    
                    // Border
                    drawRect(
                        color = EcoGreen,
                        topLeft = Offset(20f, 20f),
                        size = size.copy(width = width - 40f, height = height - 40f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("CERTIFICATE OF ACHIEVEMENT", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = OceanTeal)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("This is to certify that", fontSize = 12.sp, color = MistGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(studentName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepEarth)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("of $schoolName", fontSize = 14.sp, color = DeepEarth)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("has successfully achieved the rank of", fontSize = 12.sp, color = MistGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(achievement, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EcoGreen)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Date: $date", fontSize = 10.sp, color = MistGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { 
                    generatePDF(context, studentName, schoolName, achievement, date)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = EcoGreen)
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download PDF")
            }

            OutlinedButton(
                onClick = { /* Share Logic */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share")
            }
        }
    }
}

fun generatePDF(context: android.content.Context, name: String, school: String, achievement: String, date: String) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas
    val paint = Paint()

    // Background border
    paint.color = android.graphics.Color.parseColor("#2E7D32")
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 5f
    canvas.drawRect(20f, 20f, 575f, 822f, paint)

    // Text
    paint.style = Paint.Style.FILL
    paint.textAlign = Paint.Align.CENTER
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    
    paint.textSize = 24f
    canvas.drawText("CERTIFICATE OF ACHIEVEMENT", 297f, 150f, paint)

    paint.textSize = 16f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("This is to certify that", 297f, 220f, paint)

    paint.textSize = 32f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText(name, 297f, 280f, paint)

    paint.textSize = 18f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("of $school", 297f, 320f, paint)

    paint.textSize = 16f
    canvas.drawText("has successfully achieved the rank of", 297f, 400f, paint)

    paint.textSize = 28f
    paint.color = android.graphics.Color.parseColor("#2E7D32")
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText(achievement, 297f, 460f, paint)

    paint.textSize = 12f
    paint.color = android.graphics.Color.GRAY
    canvas.drawText("Date: $date", 297f, 550f, paint)

    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(null), "EcoQuest_Certificate.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "Certificate saved to Documents", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
    pdfDocument.close()
}
