package com.ecoquest.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.ecoquest.app.ui.components.BottomNavBar
import com.ecoquest.app.ui.screens.*
import com.ecoquest.app.ui.screens.games.*
import com.ecoquest.app.viewmodel.AuthViewModel
import com.ecoquest.app.viewmodel.ChatViewModel
import com.ecoquest.app.viewmodel.GamificationViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    gamificationViewModel: GamificationViewModel = viewModel(),
    // ChatViewModel created ONCE here at the top level so the same instance
    // is shared across all screens — this is the key fix for the chatbot
    chatViewModel: ChatViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute in listOf(
        "home", "courses", "quizzes", "leaderboard", "profile"
    )

    if (authState.isLoading) return

    val isLoggedIn = authState.user != null || authState.isTestUser
    val startDestination = if (isLoggedIn) "home" else "login"

    Scaffold(
        bottomBar = {
            if (showBottomNav && isLoggedIn) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onAdminLogin = { e, p -> authViewModel.adminLogin(e, p) },
                    onSignedIn = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    authState = authState
                )
            }

            composable("home") {
                HomeScreen(
                    user = authState.user,
                    chatViewModel = chatViewModel,             // ← pass shared VM
                    gamificationViewModel = gamificationViewModel,
                    onNavigateToQuiz = { topic, diff ->
                        navController.navigate("quiz/$topic/$diff")
                    },
                    onNavigateToGames = { navController.navigate("games") }
                )
            }

            composable("courses") { EcoCoursesScreen() }

            composable("quizzes") {
                QuizzesScreen(
                    onNavigateToQuiz = { t, d -> navController.navigate("quiz/$t/$d") }
                )
            }

            composable("leaderboard") { LeaderboardScreen() }

            composable("profile") {
                ProfileScreen(
                    user = authState.user,
                    onSignOut = {
                        authViewModel.signOut()
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    onViewCertificate = { navController.navigate("certificate") }
                )
            }

            composable("certificate") {
                val gamState by gamificationViewModel.userStats.collectAsState()
                CertificateScreen(
                    studentName  = "Eco Student",
                    schoolName   = "EcoQuest Academy",
                    achievement  = gamState.levelName
                )
            }

            composable(
                "quiz/{topic}/{difficulty}",
                arguments = listOf(
                    navArgument("topic")      { type = NavType.StringType },
                    navArgument("difficulty") { type = NavType.StringType }
                )
            ) { back ->
                QuizScreen(
                    topic      = back.arguments?.getString("topic")      ?: "climate",
                    difficulty = back.arguments?.getString("difficulty") ?: "easy",
                    onNavigateHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("games") {
                GamesHubScreen(
                    onNavigateToGame = { game -> navController.navigate("game/$game") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                "game/{gameId}",
                arguments = listOf(navArgument("gameId") { type = NavType.StringType })
            ) { back ->
                val gameId = back.arguments?.getString("gameId") ?: "waste"
                when (gameId) {
                    "waste"  -> WasteSegregationGame(
                        onBack      = { navController.popBackStack() },
                        gamViewModel = gamificationViewModel
                    )
                    "water"  -> WaterConservationGame(
                        onBack      = { navController.popBackStack() },
                        gamViewModel = gamificationViewModel
                    )
                    "carbon" -> CarbonFootprintGame(
                        onBack      = { navController.popBackStack() },
                        gamViewModel = gamificationViewModel
                    )
                    "energy" -> EnergyManagementGame(
                        onBack      = { navController.popBackStack() },
                        gamViewModel = gamificationViewModel
                    )
                    else -> GamesHubScreen(
                        onNavigateToGame = { g -> navController.navigate("game/$g") },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
