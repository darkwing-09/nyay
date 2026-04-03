package com.nyayasetu.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.MessageViewModel
import com.nyayasetu.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val conversationsState by viewModel.conversationsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchConversations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversations") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (conversationsState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> Text("Error loading conversations", modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val conversations = (conversationsState as Resource.Success).data
                    if (conversations.isEmpty()) {
                        Text("No conversations yet.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(conversations) { conv ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { onNavigateToChat(conv.id) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(conv.lawyer_name, style = MaterialTheme.typography.titleMedium)
                                        Text(conv.last_message, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
