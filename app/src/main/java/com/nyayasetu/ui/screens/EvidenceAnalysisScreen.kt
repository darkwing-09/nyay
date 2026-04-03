package com.nyayasetu.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.AdvancedFirViewModel
import com.nyayasetu.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvidenceAnalysisScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedFirViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    val evidenceState by viewModel.evidenceState.collectAsState()

    val multipleFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> -> selectedUris = uris }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evidence Analysis") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("${selectedUris.size} files selected")
                    Button(onClick = { multipleFilePicker.launch("*/*") }) {
                        Text("Select Evidence Files")
                    }
                }
            }

            Button(
                onClick = { viewModel.analyzeEvidence(context, selectedUris) },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedUris.isNotEmpty() && evidenceState !is Resource.Loading
            ) {
                if (evidenceState is Resource.Loading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Analyze Evidence")
            }

            if (evidenceState is Resource.Error) {
                Text((evidenceState as Resource.Error).message, color = MaterialTheme.colorScheme.error)
            }
            if (evidenceState is Resource.Success) {
                Text("Analysis completed!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
