package com.nyayasetu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nyayasetu.ui.screens.HomeScreen
import com.nyayasetu.ui.screens.LoginScreen
import com.nyayasetu.ui.screens.RegisterScreen
import com.nyayasetu.ui.screens.SplashScreen

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("chat") {
            com.nyayasetu.ui.screens.ChatScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable("fir_form") {
            com.nyayasetu.ui.screens.FirFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable("ai_analysis") {
            com.nyayasetu.ui.screens.AnalysisScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable("upload_fir") {
            com.nyayasetu.ui.screens.FirUploadScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable("voice_fir") {
            com.nyayasetu.ui.screens.FirVoiceScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable("evidence_analysis") {
            com.nyayasetu.ui.screens.EvidenceAnalysisScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable("lawyers") {
            com.nyayasetu.ui.screens.LawyerListScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToProfile = { handle -> navController.navigate("lawyer_profile/$handle") }
            )
        }
        composable(
            "lawyer_profile/{handle}",
            arguments = listOf(androidx.navigation.navArgument("handle") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val handle = backStackEntry.arguments?.getString("handle") ?: return@composable
            com.nyayasetu.ui.screens.LawyerProfileScreen(
                handle = handle,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChat = { convId -> navController.navigate("chat_with_lawyer/$convId") }
            )
        }
        composable("legal_feed") {
            com.nyayasetu.ui.screens.FeedScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable("conversations") {
            com.nyayasetu.ui.screens.ConversationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToChat = { convId -> navController.navigate("chat_with_lawyer/$convId") }
            )
        }
        composable(
            "chat_with_lawyer/{id}",
            arguments = listOf(androidx.navigation.navArgument("id") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            com.nyayasetu.ui.screens.ChatWithLawyerScreen(
                conversationId = id,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
