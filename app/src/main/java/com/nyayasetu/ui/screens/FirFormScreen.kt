package com.nyayasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.FirViewModel
import com.nyayasetu.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: FirViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val previewState by viewModel.previewState.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    var showPreviewDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(previewState) {
        if (previewState is Resource.Success) {
            showPreviewDialog = true
        }
    }
    
    LaunchedEffect(submitState) {
        if (submitState is Resource.Success) {
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filing an FIR") },
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
            Text("FIR Report Details", style = MaterialTheme.typography.titleLarge)
            
            formState.formError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = formState.complainantName,
                onValueChange = { viewModel.updateFormState(formState.copy(complainantName = it)) },
                label = { Text("Complainant Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.parentName,
                onValueChange = { viewModel.updateFormState(formState.copy(parentName = it)) },
                label = { Text("Parent's Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.address,
                onValueChange = { viewModel.updateFormState(formState.copy(address = it)) },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.contactNumber,
                onValueChange = { viewModel.updateFormState(formState.copy(contactNumber = it)) },
                label = { Text("Contact Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.policeStation,
                onValueChange = { viewModel.updateFormState(formState.copy(policeStation = it)) },
                label = { Text("Police Station") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = formState.incidentDate,
                    onValueChange = { viewModel.updateFormState(formState.copy(incidentDate = it)) },
                    label = { Text("Incident Date") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = formState.incidentTime,
                    onValueChange = { viewModel.updateFormState(formState.copy(incidentTime = it)) },
                    label = { Text("Incident Time") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = formState.incidentLocation,
                onValueChange = { viewModel.updateFormState(formState.copy(incidentLocation = it)) },
                label = { Text("Incident Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.incidentDescription,
                onValueChange = { viewModel.updateFormState(formState.copy(incidentDescription = it)) },
                label = { Text("Incident Description *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 10
            )

            DynamicListSection("Accused Details", formState.accusedDetails) { list ->
                viewModel.updateFormState(formState.copy(accusedDetails = list))
            }
            
            DynamicListSection("Witness Details", formState.witnessDetails) { list ->
                viewModel.updateFormState(formState.copy(witnessDetails = list))
            }
            
            DynamicListSection("Evidence Information", formState.evidenceInformation) { list ->
                viewModel.updateFormState(formState.copy(evidenceInformation = list))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.previewFir() },
                    modifier = Modifier.weight(1f),
                    enabled = previewState !is Resource.Loading && submitState !is Resource.Loading
                ) {
                    if (previewState is Resource.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Preview FIR")
                    }
                }
                
                Button(
                    onClick = { viewModel.submitFir() },
                    modifier = Modifier.weight(1f),
                    enabled = submitState !is Resource.Loading && previewState !is Resource.Loading
                ) {
                    if (submitState is Resource.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Submit FIR")
                    }
                }
            }

            if (previewState is Resource.Error) {
                Text((previewState as Resource.Error).message, color = MaterialTheme.colorScheme.error)
                Button(onClick = { viewModel.resetPreviewState() }) { Text("Retry Preview") }
            }
            
            if (submitState is Resource.Error) {
                Text((submitState as Resource.Error).message, color = MaterialTheme.colorScheme.error)
                Button(onClick = { viewModel.resetSubmitState() }) { Text("Retry Submit") }
            }
        }
    }

    if (showPreviewDialog) {
        AlertDialog(
            onDismissRequest = { 
                showPreviewDialog = false 
                viewModel.resetPreviewState()
            },
            title = { Text("FIR Preview") },
            text = {
                val data = (previewState as? Resource.Success)?.data
                Text(data?.preview_text ?: data?.generated_draft ?: "No preview generated... Check formatting.", style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = { 
                    showPreviewDialog = false
                    viewModel.resetPreviewState()
                }) { Text("Close") }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { 
                showSuccessDialog = false 
                viewModel.resetSubmitState()
                onNavigateBack()
            },
            title = { Text("FIR Submitted") },
            text = {
                val data = (submitState as? Resource.Success)?.data
                Text("Your FIR has been filed successfully. Reference status: ${data?.status ?: "Submitted"}")
            },
            confirmButton = {
                TextButton(onClick = { 
                    showSuccessDialog = false
                    viewModel.resetSubmitState()
                    onNavigateBack()
                }) { Text("Done") }
            }
        )
    }
}

@Composable
fun DynamicListSection(
    title: String,
    items: List<String>,
    onItemsChange: (List<String>) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { onItemsChange(items + "") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = item,
                    onValueChange = { newValue ->
                        val newList = items.toMutableList()
                        newList[index] = newValue
                        onItemsChange(newList)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    val newList = items.toMutableList()
                    newList.removeAt(index)
                    onItemsChange(newList)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}
