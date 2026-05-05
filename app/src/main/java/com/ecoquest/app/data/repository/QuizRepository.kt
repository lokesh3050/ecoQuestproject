package com.ecoquest.app.data.repository

import com.ecoquest.app.data.Question
import com.ecoquest.app.data.QuizData

/**
 * Repository for handling quiz data.
 */
class QuizRepository {
    
    fun getQuestions(topic: String, difficulty: String): List<Question> {
        return QuizData.getQuestions(topic, difficulty)
    }

    fun getTopics(): List<Pair<String, String>> {
        return QuizData.getTopics()
    }
}
