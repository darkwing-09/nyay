package com.nyayasetu.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.AnalysisViewModel
import com.nyayasetu.utils.Resource
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val analysisState by viewModel.analysisState.collectAsState()
    val strengthState by viewModel.strengthState.collectAsState()
    val draftState by viewModel.draftState.collectAsState()
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Legal Intelligence") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Case Details", style = MaterialTheme.typography.titleLarge)
            
            formState.formError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = formState.description,
                onValueChange = { viewModel.updateFormState(formState.copy(description = it)) },
                label = { Text("Incident Description *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 10
            )

            OutlinedTextField(
                value = formState.location,
                onValueChange = { viewModel.updateFormState(formState.copy(location = it)) },
                label = { Text("Location *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.incidentDate,
                onValueChange = { viewModel.updateFormState(formState.copy(incidentDate = it)) },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            DynamicListSection("People Involved", formState.peopleInvolved) { list ->
                viewModel.updateFormState(formState.copy(peopleInvolved = list))
            }
            
            DynamicListSection("Evidence Available", formState.evidence) { list ->
                viewModel.updateFormState(formState.copy(evidence = list))
            }

            val isLoading = analysisState is Resource.Loading || strengthState is Resource.Loading || draftState is Resource.Loading

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { viewModel.analyzeCase() }, enabled = !isLoading, modifier = Modifier.weight(1f)) {
                    Text("Analyze", maxLines = 1)
                }
                Button(onClick = { viewModel.analyzeStrength() }, enabled = !isLoading, modifier = Modifier.weight(1f)) {
                    Text("Strength", maxLines = 1)
                }
                Button(onClick = { viewModel.generateDraft() }, enabled = !isLoading, modifier = Modifier.weight(1f)) {
                    Text("Draft", maxLines = 1)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            val isError = analysisState is Resource.Error || strengthState is Resource.Error || draftState is Resource.Error
            if (isError) {
                val errorMsg = (analysisState as? Resource.Error)?.message 
                    ?: (strengthState as? Resource.Error)?.message 
                    ?: (draftState as? Resource.Error)?.message ?: "Unknown error"
                    
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(errorMsg, color = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retryLastAction() }) {
                            Text("Retry Action")
                        }
                    }
                }
            }

            if (analysisState is Resource.Success) {
                val data = (analysisState as Resource.Success).data
                ResultCard("Case Analysis") {
                    Text("Summary: ${data.summary ?: data.data?.jsonObject?.get("summary")?.jsonPrimitive?.contentOrNull ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Laws: ${data.applicable_laws?.joinToString() ?: data.data?.jsonObject?.get("applicable_laws")?.toString() ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (strengthState is Resource.Success) {
                val data = (strengthState as Resource.Success).data
                ResultCard("Case Strength") {
                    Text("Score: ${data.score ?: data.data?.jsonObject?.get("score")?.jsonPrimitive?.contentOrNull ?: "N/A"}/10", style = MaterialTheme.typography.titleMedium)
                    Text("Verdict: ${data.verdict ?: data.data?.jsonObject?.get("verdict")?.jsonPrimitive?.contentOrNull ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (draftState is Resource.Success) {
                val data = (draftState as Resource.Success).data
                ResultCard("Generated Draft") {
                    val text = data.draft_text ?: data.data?.jsonObject?.get("draft_text")?.jsonPrimitive?.contentOrNull ?: "N/A"
                    Text(text, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Legal Draft", text)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Copy Draft")
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
