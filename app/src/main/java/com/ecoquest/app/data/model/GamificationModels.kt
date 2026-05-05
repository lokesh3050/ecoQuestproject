package com.ecoquest.app.data.model

data class UserStats(
    val ecoPoints: Int = 0,
    val xp: Int = 0,
    val level: Int = 1,
    val levelName: String = "Seedling",
    val currentStreak: Int = 0,
    val lastActivityTimestamp: Long = 0,
    val streakShields: Int = 0,
    val totalQuizzesCompleted: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val totalGamesCompleted: Int = 0,
    val lastDailyCheckIn: Long = 0,
    val completedChallengeIds: Set<String> = emptySet(),
    val lastChallengeResetTimestamp: Long = 0
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String, // Emoji or resource name
    val category: BadgeCategory,
    val isEarned: Boolean = false,
    val earnedTimestamp: Long? = null
)

enum class BadgeCategory {
    KNOWLEDGE, ACTION, COMMUNITY, RARE
}

data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val pointsReward: Int,
    val xpReward: Int,
    val isCompleted: Boolean = false,
    val type: ChallengeType,
    val question: String? = null,
    val correctAnswer: String? = null
)

enum class ChallengeType {
    QUIZ, ACTION, INPUT
}
