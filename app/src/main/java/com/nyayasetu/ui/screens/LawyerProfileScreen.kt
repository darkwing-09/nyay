package com.nyayasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.LawyerViewModel
import com.nyayasetu.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerProfileScreen(
    handle: String,
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    viewModel: LawyerViewModel = hiltViewModel(),
    messageViewModel: com.nyayasetu.ui.viewmodel.MessageViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()
    val followState by viewModel.followState.collectAsState()

    LaunchedEffect(handle) {
        viewModel.fetchLawyerProfile(handle)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            when (profileState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val profile = (profileState as Resource.Success).data ?: return@Box
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(profile.lawyer.full_name, style = MaterialTheme.typography.headlineMedium)
                        Text(profile.lawyer.specialization, style = MaterialTheme.typography.titleMedium)
                        Text(profile.lawyer.bio, style = MaterialTheme.typography.bodyLarge)
                        Text("Followers: ${profile.follower_count}")
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.followLawyer(handle) },
                                enabled = followState !is Resource.Loading
                            ) {
                                Text(if (profile.is_following) "Unfollow" else "Follow")
                            }
                            Button(onClick = { 
                                messageViewModel.startConversation(handle, "Hello, I need legal advice.") { convId ->
                                    onNavigateToChat(convId)
                                }
                            }) {
                                Text("Message")
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
