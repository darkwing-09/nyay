package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.TokenManager
import com.nyayasetu.data.models.FirCreateResponse
import com.nyayasetu.data.models.FirPreviewResponse
import com.nyayasetu.data.models.FirRequest
import com.nyayasetu.data.repository.FirRepository
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FirFormState(
    val complainantName: String = "",
    val parentName: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val policeStation: String = "",
    val incidentDate: String = "",
    val incidentTime: String = "",
    val incidentLocation: String = "",
    val incidentDescription: String = "",
    val accusedDetails: List<String> = emptyList(),
    val witnessDetails: List<String> = emptyList(),
    val evidenceInformation: List<String> = emptyList(),
    val formError: String? = null
)

@HiltViewModel
class FirViewModel @Inject constructor(
    private val repository: FirRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _formState = MutableStateFlow(FirFormState())
    val formState: StateFlow<FirFormState> = _formState.asStateFlow()

    private val _previewState = MutableStateFlow<Resource<FirPreviewResponse>>(Resource.Idle())
    val previewState: StateFlow<Resource<FirPreviewResponse>> = _previewState.asStateFlow()

    private val _submitState = MutableStateFlow<Resource<FirCreateResponse>>(Resource.Idle())
    val submitState: StateFlow<Resource<FirCreateResponse>> = _submitState.asStateFlow()

    fun updateFormState(newState: FirFormState) {
        _formState.value = newState
    }

    private fun validateForm(): FirRequest? {
        val state = _formState.value
        val complainantName = state.complainantName.trim()
        var description = state.incidentDescription.trim()
        val contactNumber = state.contactNumber.trim()
        val incDate = state.incidentDate.trim()
        val incTime = state.incidentTime.trim()

        if (complainantName.isEmpty()) {
            _formState.value = state.copy(formError = "Complainant name cannot be empty")
            return null
        }
        if (description.isEmpty()) {
            _formState.value = state.copy(formError = "Incident description cannot be empty")
            return null
        }
        if (description.length > 2000) {
            description = description.take(2000)
        }
        if (contactNumber.isNotEmpty() && !contactNumber.matches(Regex("^[0-9]{10,15}\$"))) {
            _formState.value = state.copy(formError = "Contact number must be strictly 10 to 15 digits")
            return null
        }
        if (incDate.isNotEmpty() && !incDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2}\$"))) {
            _formState.value = state.copy(formError = "Date must be YYYY-MM-DD")
            return null
        }
        if (incTime.isNotEmpty() && !incTime.matches(Regex("^\\d{2}:\\d{2}\$"))) {
            _formState.value = state.copy(formError = "Time must be HH:MM")
            return null
        }

        _formState.value = state.copy(formError = null)

        return FirRequest(
            complainant_name = complainantName,
            parent_name = state.parentName.trim(),
            address = state.address.trim(),
            contact_number = contactNumber,
            police_station = state.policeStation.trim(),
            incident_date = incDate,
            incident_time = incTime,
            incident_location = state.incidentLocation.trim(),
            incident_description = description,
            accused_details = state.accusedDetails.filter { it.isNotBlank() },
            witness_details = state.witnessDetails.filter { it.isNotBlank() },
            evidence_information = state.evidenceInformation.filter { it.isNotBlank() },
            draft_role = "citizen_application",
            language = "en",
            user_id = tokenManager.getUserId() ?: "unknown_user"
        )
    }

    fun previewFir() {
        if (_previewState.value is Resource.Loading || _submitState.value is Resource.Loading) return
        val request = validateForm() ?: return
        
        viewModelScope.launch {
            repository.previewFir(request).collect { result ->
                _previewState.value = result
            }
        }
    }

    fun submitFir() {
        if (_previewState.value is Resource.Loading || _submitState.value is Resource.Loading) return
        val request = validateForm() ?: return
        
        viewModelScope.launch {
            repository.createFir(request).collect { result ->
                _submitState.value = result
            }
        }
    }

    fun resetPreviewState() {
        _previewState.value = Resource.Idle()
    }

    fun resetSubmitState() {
        _submitState.value = Resource.Idle()
    }
}
