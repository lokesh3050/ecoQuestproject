package com.ecoquest.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ecoquest.app.data.model.Badge
import com.ecoquest.app.data.model.BadgeCategory
import com.ecoquest.app.data.model.DailyChallenge
import com.ecoquest.app.data.model.UserStats
import com.ecoquest.app.data.model.ChallengeType
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit

private val Context.dataStore by preferencesDataStore(name = "gamification_prefs")

class GamificationRepository(private val context: Context) {
    private val gson = Gson()
    private val statsKey = stringPreferencesKey("user_stats")

    val userStats: Flow<UserStats> = context.dataStore.data.map { preferences ->
        val statsJson = preferences[statsKey]
        if (statsJson != null) {
            val stats = gson.fromJson(statsJson, UserStats::class.java)
            val decayedStats = applyEcoPointsDecay(stats)
            resetDailyChallengesIfNeeded(decayedStats)
        } else {
            UserStats()
        }
    }

    private fun resetDailyChallengesIfNeeded(stats: UserStats): UserStats {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        val lastReset = Calendar.getInstance().apply { timeInMillis = stats.lastChallengeResetTimestamp }
        
        val isDifferentDay = calendar.get(Calendar.DAY_OF_YEAR) != lastReset.get(Calendar.DAY_OF_YEAR) || 
                            calendar.get(Calendar.YEAR) != lastReset.get(Calendar.YEAR)
        
        return if (isDifferentDay) {
            stats.copy(completedChallengeIds = emptySet(), lastChallengeResetTimestamp = now)
        } else {
            stats
        }
    }

    private fun applyEcoPointsDecay(stats: UserStats): UserStats {
        val now = System.currentTimeMillis()
        val daysInactive = TimeUnit.MILLISECONDS.toDays(now - stats.lastActivityTimestamp)
        
        return if (daysInactive >= 7 && stats.ecoPoints > 0) {
            val decayAmount = (stats.ecoPoints * 0.05 * (daysInactive / 7)).toInt()
            stats.copy(ecoPoints = (stats.ecoPoints - decayAmount).coerceAtLeast(0))
        } else {
            stats
        }
    }

    suspend fun updateStats(update: (UserStats) -> UserStats) {
        context.dataStore.edit { preferences ->
            val currentStats = preferences[statsKey]?.let { 
                gson.fromJson(it, UserStats::class.java) 
            } ?: UserStats()
            
            val newStats = update(currentStats).copy(lastActivityTimestamp = System.currentTimeMillis())
            preferences[statsKey] = gson.toJson(calculateLevel(newStats))
        }
    }

    private fun calculateLevel(stats: UserStats): UserStats {
        val xpPerLevel = 1000
        val newLevel = (stats.xp / xpPerLevel) + 1
        val levelName = when {
            newLevel >= 50 -> "Eco Legend"
            newLevel >= 30 -> "Earth Warrior"
            newLevel >= 15 -> "Green Guardian"
            newLevel >= 5 -> "Sapling"
            else -> "Seedling"
        }
        return stats.copy(level = newLevel, levelName = levelName)
    }

    suspend fun recordActivity() {
        updateStats { current ->
            val now = Calendar.getInstance()
            val last = Calendar.getInstance().apply { timeInMillis = current.lastActivityTimestamp }
            
            val isNextDay = now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR) || 
                            now.get(Calendar.YEAR) != last.get(Calendar.YEAR)
            
            if (isNextDay) {
                val diff = now.timeInMillis - last.timeInMillis
                val daysDiff = TimeUnit.MILLISECONDS.toDays(diff)
                
                when {
                    daysDiff == 1L -> current.copy(currentStreak = current.currentStreak + 1)
                    current.streakShields > 0 -> current.copy(streakShields = current.streakShields - 1)
                    else -> current.copy(currentStreak = 1)
                }
            } else {
                current
            }
        }
    }

    fun getBadges(): List<Badge> {
        return listOf(
            Badge("k1", "Quiz Master", "Complete 10 quizzes", "🏆", BadgeCategory.KNOWLEDGE),
            Badge("a1", "Water Saver", "Log water saving task", "💧", BadgeCategory.ACTION),
            Badge("r1", "Monsoon Warrior", "Active during monsoon", "⛈️", BadgeCategory.RARE),
            Badge("c1", "Eco Friend", "Help 5 users", "🤝", BadgeCategory.COMMUNITY)
        )
    }

    fun getDailyChallenges(completedIds: Set<String>): List<DailyChallenge> {
        return listOf(
            DailyChallenge(
                id = "d1", 
                title = "Species ID", 
                description = "Name 3 endemic species of Sundarbans", 
                pointsReward = 50, 
                xpReward = 100, 
                type = ChallengeType.INPUT,
                question = "Name an endemic species of Sundarbans (Hint: Tiger)",
                correctAnswer = "Royal Bengal Tiger",
                isCompleted = completedIds.contains("d1")
            ),
            DailyChallenge(
                id = "d2", 
                title = "Water Footprint", 
                description = "Did you save water today by using a bucket instead of a shower?", 
                pointsReward = 30, 
                xpReward = 60, 
                type = ChallengeType.ACTION,
                isCompleted = completedIds.contains("d2")
            ),
            DailyChallenge(
                id = "d3", 
                title = "Recycle Quiz", 
                description = "Is plastic biodegradable?", 
                pointsReward = 20, 
                xpReward = 40, 
                type = ChallengeType.QUIZ,
                question = "Is plastic biodegradable?",
                correctAnswer = "No",
                isCompleted = completedIds.contains("d3")
            )
        )
    }
}
