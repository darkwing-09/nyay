package com.nyayasetu.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.nyayasetu.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // ── Auth flow ───────────────────────────────────────
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToDashboard = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigateUp() }
            )
        }

        // ── Home (dashboard grid) ───────────────────────────
        composable("home") {
            val authViewModel: com.nyayasetu.ui.viewmodel.AuthViewModel =
                androidx.hilt.navigation.compose.hiltViewModel()
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        // ── Feature screens ─────────────────────────────────
        composable("fir_form") {
            FirFormScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("fir_upload") {
            FirUploadScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("fir_voice") {
            FirVoiceScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("analysis") {
            AnalysisScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("evidence_analysis") {
            EvidenceAnalysisScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("lawyers") {
            LawyerListScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToProfile = { handle ->
                    navController.navigate("lawyer_profile/$handle")
                }
            )
        }

        composable(
            "lawyer_profile/{handle}",
            arguments = listOf(
                androidx.navigation.navArgument("handle") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val handle = backStackEntry.arguments?.getString("handle") ?: ""
            LawyerProfileScreen(
                handle = handle,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChat = { convId ->
                    navController.navigate("chat_with_lawyer/$convId")
                }
            )
        }

        composable("feed") {
            FeedScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("conversations") {
            ConversationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChat = { convId ->
                    navController.navigate("chat_with_lawyer/$convId")
                }
            )
        }

        composable(
            "chat_with_lawyer/{id}",
            arguments = listOf(
                androidx.navigation.navArgument("id") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ChatWithLawyerScreen(
                conversationId = id,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable("chat") {
            ChatScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}
