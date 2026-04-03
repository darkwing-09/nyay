package com.nyayasetu.ui.screens

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material.icons.filled.Menu
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyayasetu.ui.components.DashboardFeatureCard

private data class DashboardModule(
    val title: String,
    val description: String,
)

private val dashboardModules = listOf(
    DashboardModule(
        title = "Authentication foundation",
        description = "Compose-first login and registration flows are wired and ready for future auth APIs.",
    ),
    DashboardModule(
        title = "Network foundation",
        description = "Retrofit, OkHttp, Kotlinx Serialization, and Hilt are configured for scalable API integration.",
    ),
    DashboardModule(
        title = "Architecture foundation",
        description = "MVVM, StateFlow, Resource wrappers, repository safety, and error mapping are in place.",
    ),
    DashboardModule(
        title = "Navigation foundation",
        description = "Splash, Login, Register, and Dashboard routes are connected through Navigation Compose.",
    ),
)

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onNavigateToChat: () -> Unit = {},
    onNavigateToFir: () -> Unit = {},
    onNavigateToAnalysis: () -> Unit = {},
    onNavigateToUpload: () -> Unit = {},
    onNavigateToVoice: () -> Unit = {},
    onNavigateToEvidence: () -> Unit = {},
    onNavigateToLawyers: () -> Unit = {},
    onNavigateToFeed: () -> Unit = {},
    onNavigateToConversations: () -> Unit = {}
) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "Production-ready Android foundation",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "This dashboard confirms that the app shell, dependency graph, and screen navigation are ready to support the full Nyaya Setu API surface later.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                    )
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = "Next implementation phase",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                text = "Repositories, DTOs, use cases, API services, and authenticated flows can now be layered on top without restructuring the app.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.86f),
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }

            items(dashboardModules) { module ->
                DashboardFeatureCard(
                    title = module.title,
                    description = module.description,
                )
            }

            item {
                Button(
                    onClick = onNavigateToFir,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("File a Manual FIR")
                }
            }
            
            item {
                Button(
                    onClick = onNavigateToAnalysis,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("AI Case Analysis & Drafting")
                }
            }

            item {
                Button(
                    onClick = onNavigateToUpload,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Upload Document FIR")
                }
            }

            item {
                Button(
                    onClick = onNavigateToVoice,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Record Voice FIR")
                }
            }

            item {
                Button(
                    onClick = onNavigateToEvidence,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Batch Evidence Analysis")
                }
            }

            item {
                Button(
                    onClick = onNavigateToLawyers,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Find Lawyers")
                }
            }

            item {
                Button(
                    onClick = onNavigateToFeed,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Legal Feed")
                }
            }

            item {
                Button(
                    onClick = onNavigateToConversations,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("My Conversations")
                }
            }

            item {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Foundation status: ready for API integration.",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
}
