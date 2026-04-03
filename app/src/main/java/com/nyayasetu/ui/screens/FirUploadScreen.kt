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
fun FirUploadScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedFirViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var policeStation by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    val uploadState by viewModel.uploadState.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload FIR Document") },
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
            OutlinedTextField(
                value = policeStation,
                onValueChange = { policeStation = it },
                label = { Text("Police Station *") },
                modifier = Modifier.fillMaxWidth()
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(if (selectedUri != null) "File Selected" else "No file selected")
                    Button(onClick = { filePickerLauncher.launch("*/*") }) {
                        Text("Select Document (PDF/Image)")
                    }
                }
            }

            Button(
                onClick = { 
                    selectedUri?.let { viewModel.uploadFir(context, it, policeStation) }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedUri != null && policeStation.isNotBlank() && uploadState !is Resource.Loading
            ) {
                if (uploadState is Resource.Loading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Upload FIR")
            }

            if (uploadState is Resource.Error) {
                Text((uploadState as Resource.Error).message, color = MaterialTheme.colorScheme.error)
            }
            if (uploadState is Resource.Success) {
                Text("FIR Uploaded successfully!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
