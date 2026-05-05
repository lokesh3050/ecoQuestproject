package com.ecoquest.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ecoquest.app.data.model.Badge
import com.ecoquest.app.data.model.DailyChallenge
import com.ecoquest.app.data.model.UserStats
import com.ecoquest.app.data.repository.GamificationRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map

class GamificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GamificationRepository(application)

    val userStats: StateFlow<UserStats> = repository.userStats.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserStats()
    )

    fun addEcoPoints(amount: Int) {
        viewModelScope.launch {
            repository.updateStats { it.copy(ecoPoints = it.ecoPoints + amount) }
        }
    }

    fun addXP(amount: Int) {
        viewModelScope.launch {
            repository.updateStats { it.copy(xp = it.xp + amount) }
        }
    }

    fun recordActivity() {
        viewModelScope.launch {
            repository.recordActivity()
        }
    }

    fun completeChallenge(challenge: DailyChallenge) {
        viewModelScope.launch {
            repository.updateStats { stats ->
                if (!stats.completedChallengeIds.contains(challenge.id)) {
                    stats.copy(
                        ecoPoints = stats.ecoPoints + challenge.pointsReward,
                        xp = stats.xp + challenge.xpReward,
                        completedChallengeIds = stats.completedChallengeIds + challenge.id
                    )
                } else {
                    stats
                }
            }
        }
    }

    fun getBadges(): List<Badge> = repository.getBadges()
    
    fun getDailyChallenges(): List<DailyChallenge> {
        val completedIds = userStats.value.completedChallengeIds
        return repository.getDailyChallenges(completedIds)
    }
}
