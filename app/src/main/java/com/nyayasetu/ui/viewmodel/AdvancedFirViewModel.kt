package com.nyayasetu.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.TokenManager
import com.nyayasetu.data.models.AdvancedFirResponse
import com.nyayasetu.data.models.JurisdictionRequest
import com.nyayasetu.data.models.SectionPredictionRequest
import com.nyayasetu.data.repository.AdvancedFirRepository
import com.nyayasetu.utils.FileUtils
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AdvancedFirViewModel @Inject constructor(
    private val repository: AdvancedFirRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uploadState = MutableStateFlow<Resource<AdvancedFirResponse>>(Resource.Idle())
    val uploadState = _uploadState.asStateFlow()

    private val _voiceState = MutableStateFlow<Resource<AdvancedFirResponse>>(Resource.Idle())
    val voiceState = _voiceState.asStateFlow()

    private val _evidenceState = MutableStateFlow<Resource<AdvancedFirResponse>>(Resource.Idle())
    val evidenceState = _evidenceState.asStateFlow()

    private val _sectionState = MutableStateFlow<Resource<AdvancedFirResponse>>(Resource.Idle())
    val sectionState = _sectionState.asStateFlow()

    private val _jurisdictionState = MutableStateFlow<Resource<AdvancedFirResponse>>(Resource.Idle())
    val jurisdictionState = _jurisdictionState.asStateFlow()

    fun uploadFir(context: Context, fileUri: Uri, policeStation: String) {
        if (_uploadState.value is Resource.Loading) return
        if (policeStation.isBlank()) {
            _uploadState.value = Resource.Error("Police station required.")
            return
        }

        val file = FileUtils.getFileFromUri(context, fileUri)
        if (file == null) {
            _uploadState.value = Resource.Error("Failed to read file.")
            return
        }
        if (!FileUtils.isFileSizeValid(file)) {
            file.delete()
            _uploadState.value = Resource.Error("File is too large (max 10MB).")
            return
        }
        if (!FileUtils.isValidMimeType(context, fileUri)) {
            file.delete()
            _uploadState.value = Resource.Error("Invalid file type.")
            return
        }

        val mimeType = FileUtils.getMimeType(context, fileUri)
        val filePart = FileUtils.buildFilePart("complaint_file", file, mimeType)
        val policeStationPart = FileUtils.buildTextPart("police_station", policeStation.trim())
        val draftRolePart = FileUtils.buildTextPart("draft_role", "citizen_application")
        val languagePart = FileUtils.buildTextPart("language", "en")
        val userIdPart = FileUtils.buildTextPart("user_id", tokenManager.getUserId() ?: "unknown_user")

        viewModelScope.launch {
            repository.uploadFir(filePart, policeStationPart, draftRolePart, languagePart, userIdPart)
                .collect { result ->
                    _uploadState.value = result
                    if (result !is Resource.Loading) {
                        file.delete() // Cleanup
                    }
                }
        }
    }

    fun uploadVoiceFir(audioFile: File, policeStation: String, complainantName: String) {
        if (_voiceState.value is Resource.Loading) return
        if (policeStation.isBlank() || complainantName.isBlank()) {
            _voiceState.value = Resource.Error("Police station and complainant name required.")
            return
        }
        if (!FileUtils.isFileSizeValid(audioFile)) {
            audioFile.delete()
            _voiceState.value = Resource.Error("Audio file is too large (max 10MB).")
            return
        }

        val filePart = FileUtils.buildFilePart("audio_file", audioFile, "audio/mpeg")
        val policeStationPart = FileUtils.buildTextPart("police_station", policeStation.trim())
        val complainantNamePart = FileUtils.buildTextPart("complainant_name", complainantName.trim())
        val draftRolePart = FileUtils.buildTextPart("draft_role", "citizen_application")
        val languagePart = FileUtils.buildTextPart("language", "en")
        val userIdPart = FileUtils.buildTextPart("user_id", tokenManager.getUserId() ?: "unknown_user")

        viewModelScope.launch {
            repository.uploadVoiceFir(filePart, null, policeStationPart, complainantNamePart, draftRolePart, languagePart, userIdPart)
                .collect { result ->
                    _voiceState.value = result
                    if (result !is Resource.Loading) {
                        audioFile.delete() // Cleanup
                    }
                }
        }
    }

    fun analyzeEvidence(context: Context, uris: List<Uri>) {
        if (_evidenceState.value is Resource.Loading) return
        if (uris.isEmpty()) return

        val filesToCleanup = mutableListOf<File>()
        val parts = mutableListOf<MultipartBody.Part>()

        for (uri in uris) {
            val file = FileUtils.getFileFromUri(context, uri) ?: continue
            val mimeType = FileUtils.getMimeType(context, uri)
            parts.add(FileUtils.buildFilePart("evidence_files", file, mimeType))
            filesToCleanup.add(file)
        }

        if (parts.isEmpty()) {
            _evidenceState.value = Resource.Error("Failed to package files.")
            return
        }

        viewModelScope.launch {
            repository.analyzeEvidence(parts).collect { result ->
                _evidenceState.value = result
                if (result !is Resource.Loading) {
                    filesToCleanup.forEach { it.delete() }
                }
            }
        }
    }

    fun predictSections(description: String) {
        if (_sectionState.value is Resource.Loading || description.isBlank()) return
        val req = SectionPredictionRequest(description.trim().take(2000))
        viewModelScope.launch {
            repository.predictSections(req).collect { _sectionState.value = it }
        }
    }

    fun predictJurisdiction(location: String) {
        if (_jurisdictionState.value is Resource.Loading || location.isBlank()) return
        val req = JurisdictionRequest(location.trim().take(100))
        viewModelScope.launch {
            repository.predictJurisdiction(req).collect { _jurisdictionState.value = it }
        }
    }
    
    fun resetStates() {
        _uploadState.value = Resource.Idle()
        _voiceState.value = Resource.Idle()
        _evidenceState.value = Resource.Idle()
        _sectionState.value = Resource.Idle()
        _jurisdictionState.value = Resource.Idle()
    }
}
