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
import com.nyayasetu.ui.viewmodel.LawyerViewModel
import com.nyayasetu.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfile: (String) -> Unit,
    viewModel: LawyerViewModel = hiltViewModel()
) {
    val listState by viewModel.lawyerListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLawyers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lawyers") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (listState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> Text("Error loading lawyers", modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    val lawyers = (listState as Resource.Success).data
                    if (lawyers.isEmpty()) {
                        Text("No lawyers found.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(lawyers) { lawyer ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { onNavigateToProfile(lawyer.handle) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(lawyer.full_name, style = MaterialTheme.typography.titleMedium)
                                        Text(lawyer.specialization, style = MaterialTheme.typography.bodyMedium)
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
