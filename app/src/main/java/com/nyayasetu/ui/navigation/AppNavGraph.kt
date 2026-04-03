package com.nyayasetu.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.nyayasetu.ui.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val noDrawerRoutes = listOf("splash", "login", "register", "")
    val showDrawer = currentRoute !in noDrawerRoutes

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        drawerContent = {
            ModalDrawerSheet {
                if (showDrawer) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Nyay Setu",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    HorizontalDivider()

                    val menuItems = listOf(
                        "Home" to "dashboard",
                        "File FIR" to "fir_form",
                        "Upload FIR Document" to "fir_upload",
                        "Record Voice FIR" to "fir_voice",
                        "AI Case Analysis" to "analysis",
                        "Batch Evidence Analysis" to "evidence_analysis",
                        "Find Lawyers" to "lawyers",
                        "Legal Feed" to "feed",
                        "My Conversations" to "conversations"
                    )

                    menuItems.forEach { (title, route) ->
                        NavigationDrawerItem(
                            label = { Text(title) },
                            selected = currentRoute == route,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(route) {
                                    launchSingleTop = true
                                    popUpTo("fir_form") { saveState = true }
                                }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = "Logout"
                            )
                        },
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("login") {
                                launchSingleTop = true
                                popUpTo("fir_form") { inclusive = true }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showDrawer) {
                    TopAppBar(
                        title = { Text("Nyay Setu") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splash") {
                    SplashScreen(
                        onNavigateToLogin = {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        },
                        onNavigateToDashboard = {
                            navController.navigate("fir_form") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }
                composable("login") {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate("register") },
                        onNavigateToDashboard = {
                            navController.navigate("fir_form") {
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
                composable("dashboard") {
                    val viewModel: com.nyayasetu.ui.viewmodel.AuthViewModel =
                        androidx.hilt.navigation.compose.hiltViewModel()
                    DashboardScreen(
                        onLogout = {
                            viewModel.logout()
                            navController.navigate("login") { popUpTo(0) }
                        },
                        onNavigateToChat = { navController.navigate("chat") },
                        onNavigateToFir = { navController.navigate("fir_form") },
                        onNavigateToAnalysis = { navController.navigate("analysis") },
                        onNavigateToUpload = { navController.navigate("fir_upload") },
                        onNavigateToVoice = { navController.navigate("fir_voice") },
                        onNavigateToEvidence = { navController.navigate("evidence_analysis") },
                        onNavigateToLawyers = { navController.navigate("lawyers") },
                        onNavigateToFeed = { navController.navigate("feed") },
                        onNavigateToConversations = { navController.navigate("conversations") }
                    )
                }
                composable("chat") {
                    ChatScreen(onNavigateBack = { navController.navigateUp() })
                }
                composable("fir_form") {
                    FirFormScreen(onNavigateBack = { navController.navigateUp() })
                }
                composable("analysis") {
                    AnalysisScreen(onNavigateBack = { navController.navigateUp() })
                }
                composable("fir_upload") {
                    FirUploadScreen(onNavigateBack = { navController.navigateUp() })
                }
                composable("fir_voice") {
                    FirVoiceScreen(onNavigateBack = { navController.navigateUp() })
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
            }
        }
    }
}
