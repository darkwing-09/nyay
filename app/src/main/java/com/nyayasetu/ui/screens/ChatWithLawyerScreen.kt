package com.nyayasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.MessageViewModel
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatWithLawyerScreen(
    conversationId: String,
    onNavigateBack: () -> Unit,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val messagesState by viewModel.messagesState.collectAsState()
    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        viewModel.openConversation(conversationId)
    }

    LaunchedEffect(messagesState) {
        if (messagesState is Resource.Success) {
            val count = (messagesState as Resource.Success).data.size
            if (count > 0) {
                listState.animateScrollToItem(count - 1)
            }
        }
    }

    LaunchedEffect(conversationId) {
        while(true) {
            delay(5000)
            viewModel.fetchConversationDetails(conversationId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (messagesState is Resource.Success) {
                    val messages = (messagesState as Resource.Success).data
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        items(messages) { msg ->
                            val alignment = if (msg.is_lawyer) Alignment.CenterStart else Alignment.CenterEnd
                            val cardColor = if (msg.is_lawyer) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
                            
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = alignment) {
                                Card(colors = CardDefaults.cardColors(containerColor = cardColor)) {
                                    Text(
                                        text = msg.content,
                                        modifier = Modifier.padding(12.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                } else if (messagesState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.sendMessage(inputMessage)
                            inputMessage = ""
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
