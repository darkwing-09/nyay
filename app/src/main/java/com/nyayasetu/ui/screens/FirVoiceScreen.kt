package com.nyayasetu.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.ui.viewmodel.AdvancedFirViewModel
import com.nyayasetu.utils.Resource
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirVoiceScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedFirViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }

    var policeStation by remember { mutableStateOf("") }
    var complainantName by remember { mutableStateOf("") }

    val voiceState by viewModel.voiceState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Start recording logic would go here if granted.
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice FIR") },
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
                value = complainantName,
                onValueChange = { complainantName = it },
                label = { Text("Complainant Name *") },
                modifier = Modifier.fillMaxWidth()
            )
            
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
                    Text(if (isRecording) "Recording..." else if (audioFile != null) "Audio recorded" else "Ready to record")
                    
                    Button(onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        if (!hasPermission) {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            return@Button
                        }

                        if (isRecording) {
                            try {
                                recorder?.stop()
                            } catch (e: Exception) { e.printStackTrace() }
                            recorder?.release()
                            recorder = null
                            isRecording = false
                        } else {
                            audioFile = File(context.cacheDir, "voice_fir_${System.currentTimeMillis()}.mp4")
                            recorder = MediaRecorder().apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                setOutputFile(audioFile!!.absolutePath)
                                try {
                                    prepare()
                                    start()
                                    isRecording = true
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }) {
                        Text(if (isRecording) "Stop Recording" else "Start Recording")
                    }
                }
            }

            Button(
                onClick = {
                    audioFile?.let { viewModel.uploadVoiceFir(it, policeStation, complainantName) }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = audioFile != null && !isRecording && policeStation.isNotBlank() && complainantName.isNotBlank() && voiceState !is Resource.Loading
            ) {
                if (voiceState is Resource.Loading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Upload Voice FIR")
            }

            if (voiceState is Resource.Error) {
                Text((voiceState as Resource.Error).message, color = MaterialTheme.colorScheme.error)
            }
            if (voiceState is Resource.Success) {
                Text("Voice FIR Uploaded successfully!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
