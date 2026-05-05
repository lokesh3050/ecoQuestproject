package com.ecoquest.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

data class ChatMessage(val text: String, val isUser: Boolean)

data class ChatState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            "Hi! I'm Sprout \uD83C\uDF31 — your EcoQuest AI guide!\n\nAsk me anything about:\n♻️ Waste management\n\uD83D\uDCA7 Water conservation\n\uD83C\uDF0D Climate change\n⚡ Renewable energy\n\uD83E\uDD8B Biodiversity",
            isUser = false
        )
    ),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Groq API request/response models
data class GroqMessage(
    val role: String,
    val content: String
)

data class GroqRequest(
    val model: String,
    val messages: List<GroqMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 500,
    val temperature: Double = 0.7,
    val stream: Boolean = false
)

data class GroqChoice(val message: GroqMessage)
data class GroqResponse(val choices: List<GroqChoice>)

class ChatViewModel : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val gson = Gson()

    // ── API credentials ────────────────────────────────────────────────────────
    // Set your Groq API key in local.properties: GROQ_API_KEY=gsk_your_key_here
    private val apiKey = "YOUR_GROQ_API_KEY"
    private val apiUrl = "https://api.groq.com/openai/v1/chat/completions"
    // llama-3.1-8b-instant — active Groq model (llama3-8b-8192 was decommissioned)
    private val model  = "llama-3.1-8b-instant"

    private val systemPrompt = """
You are Sprout 🌱, the friendly AI eco-assistant for EcoQuest — an environmental learning app for students.

Your expertise covers:
- Waste management & recycling (wet/dry/hazardous/e-waste segregation)
- Water conservation and the global water crisis
- Carbon footprint reduction and climate change
- Renewable energy (solar, wind, hydro, biomass)
- Biodiversity, wildlife conservation, and ecosystems
- Air, water, and soil pollution and solutions
- Indian environmental laws and green initiatives
- Sustainable agriculture and circular economy
- EcoQuest app features: mini-games, quizzes, daily challenges

Personality: Warm, enthusiastic, encouraging, student-friendly. Use emojis sparingly 🌿
Response style: Concise (3-5 sentences max) unless a detailed explanation is requested.
Always end with one practical eco-tip or interesting fact.
Language: Simple English suitable for school and college students.
    """.trimIndent()

    fun sendMessage(userInput: String) {
        val trimmed = userInput.trim()
        if (trimmed.isBlank()) return
        if (_chatState.value.isLoading) return

        // Append user message & set loading
        _chatState.value = _chatState.value.copy(
            messages  = _chatState.value.messages + ChatMessage(trimmed, isUser = true),
            isLoading = true,
            error     = null
        )

        viewModelScope.launch {
            try {
                // Build conversation history (system + last 16 messages)
                val history = _chatState.value.messages.takeLast(16)
                val groqMessages = mutableListOf(GroqMessage("system", systemPrompt))
                history.forEach { msg ->
                    groqMessages.add(
                        GroqMessage(
                            role    = if (msg.isUser) "user" else "assistant",
                            content = msg.text
                        )
                    )
                }

                val requestBody = GroqRequest(
                    model     = model,
                    messages  = groqMessages,
                    maxTokens = 500,
                    stream    = false
                )

                val jsonBody = gson.toJson(requestBody)
                Log.d("Sprout", "→ Groq | model=$model | msgs=${groqMessages.size}")

                val response = withContext(Dispatchers.IO) {
                    val req = Request.Builder()
                        .url(apiUrl)
                        .header("Authorization", "Bearer $apiKey")
                        .header("Content-Type",  "application/json")
                        .header("Accept",        "application/json")
                        .post(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
                        .build()
                    client.newCall(req).execute()
                }

                val bodyStr = response.body?.string() ?: ""
                Log.d("Sprout", "← code=${response.code} | body=${bodyStr.take(300)}")

                if (!response.isSuccessful) {
                    appendBot(parseError(response.code, bodyStr))
                    return@launch
                }

                if (bodyStr.isBlank()) {
                    appendBot("🌿 Got an empty response from Groq. Please try again!")
                    return@launch
                }

                // Parse response
                val groqResp = gson.fromJson(bodyStr, GroqResponse::class.java)
                val reply = groqResp?.choices
                    ?.firstOrNull()
                    ?.message
                    ?.content
                    ?.trim()

                if (reply.isNullOrBlank()) {
                    appendBot("🌿 Sprout couldn't generate a response. Please try again!")
                } else {
                    appendBot(reply)
                }

            } catch (e: java.net.SocketTimeoutException) {
                Log.e("Sprout", "Timeout: ${e.message}")
                appendBot("⏱️ Request timed out — Groq is taking too long. Please try again!")
            } catch (e: java.net.UnknownHostException) {
                Log.e("Sprout", "No internet: ${e.message}")
                appendBot("📵 No internet connection. Please check your WiFi or mobile data!")
            } catch (e: javax.net.ssl.SSLException) {
                Log.e("Sprout", "SSL error: ${e.message}")
                appendBot("🔒 Secure connection failed. Please try again!")
            } catch (e: java.io.IOException) {
                Log.e("Sprout", "IO error: ${e.message}")
                appendBot("🔌 Network error: ${e.message?.take(60)}. Please check your connection!")
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("Sprout", "JSON parse error: ${e.message}")
                appendBot("🌿 Couldn't read the response. Please try again!")
            } catch (e: Exception) {
                Log.e("Sprout", "Unexpected error: ${e.javaClass.simpleName}: ${e.message}", e)
                appendBot("🌿 Something went wrong (${e.javaClass.simpleName}). Please try again!")
            }
        }
    }

    private fun appendBot(text: String) {
        _chatState.value = _chatState.value.copy(
            messages  = _chatState.value.messages + ChatMessage(text, isUser = false),
            isLoading = false,
            error     = null
        )
    }

    private fun parseError(code: Int, body: String): String {
        Log.e("Sprout", "API error $code: $body")
        // Try to extract Groq's error message
        val groqMsg = try {
            JsonParser.parseString(body)
                .asJsonObject
                .getAsJsonObject("error")
                ?.get("message")
                ?.asString
        } catch (_: Exception) { null }

        return when (code) {
            400  -> "❌ Bad request — ${groqMsg ?: "Invalid input sent to Groq."}"
            401  -> "🔑 API key is invalid or expired. Please contact support."
            403  -> "🚫 Access denied. API key may lack permissions."
            404  -> "🔍 Model not found. Please contact support."
            413  -> "📝 Message too long. Please shorten your question!"
            422  -> "⚠️ Unprocessable request — ${groqMsg ?: "Please rephrase your question."}"
            429  -> "⏳ Rate limit reached — please wait a moment and try again!"
            500, 502, 503, 504 ->
                "🌐 Groq servers are temporarily unavailable. Please try again shortly!"
            else -> "❌ API error ($code): ${groqMsg ?: "Please try again."}"
        }
    }

    fun clearChat() {
        _chatState.value = ChatState()
    }
}
