package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.TokenManager
import com.nyayasetu.data.models.*
import com.nyayasetu.data.repository.AnalysisRepository
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalysisFormState(
    val description: String = "",
    val location: String = "",
    val incidentDate: String = "",
    val peopleInvolved: List<String> = emptyList(),
    val evidence: List<String> = emptyList(),
    val formError: String? = null
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: AnalysisRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _formState = MutableStateFlow(AnalysisFormState())
    val formState: StateFlow<AnalysisFormState> = _formState.asStateFlow()

    private val _analysisState = MutableStateFlow<Resource<AnalysisResponse>>(Resource.Idle())
    val analysisState: StateFlow<Resource<AnalysisResponse>> = _analysisState.asStateFlow()

    private val _strengthState = MutableStateFlow<Resource<AnalysisResponse>>(Resource.Idle())
    val strengthState: StateFlow<Resource<AnalysisResponse>> = _strengthState.asStateFlow()

    private val _draftState = MutableStateFlow<Resource<AnalysisResponse>>(Resource.Idle())
    val draftState: StateFlow<Resource<AnalysisResponse>> = _draftState.asStateFlow()
    
    private var lastAction: (() -> Unit)? = null

    fun updateFormState(newState: AnalysisFormState) {
        _formState.value = newState
    }

    private fun validateInputs(): Boolean {
        val state = _formState.value
        val desc = state.description.trim()
        val loc = state.location.trim()
        val incDate = state.incidentDate.trim()

        if (desc.isEmpty()) {
            _formState.value = state.copy(formError = "Description cannot be empty")
            return false
        }
        if (loc.isEmpty()) {
            _formState.value = state.copy(formError = "Location cannot be empty")
            return false
        }
        if (incDate.isNotEmpty() && !incDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2}\$"))) {
            _formState.value = state.copy(formError = "Date must be YYYY-MM-DD")
            return false
        }

        _formState.value = state.copy(formError = null, description = desc.take(2000))
        return true
    }

    fun analyzeCase() {
        if (_analysisState.value is Resource.Loading) return
        if (!validateInputs()) return
        
        lastAction = { analyzeCase() }

        val state = _formState.value
        val request = CaseAnalysisRequest(
            incident_description = state.description,
            location = state.location,
            incident_date = state.incidentDate,
            people_involved = state.peopleInvolved.filter { it.isNotBlank() },
            evidence = state.evidence.filter { it.isNotBlank() },
            language = "en",
            user_id = tokenManager.getUserId() ?: "unknown_user"
        )
        
        viewModelScope.launch {
            repository.analyzeCase(request).collect {
                _analysisState.value = it
            }
        }
    }

    fun analyzeStrength() {
        if (_strengthState.value is Resource.Loading) return
        if (!validateInputs()) return
        
        lastAction = { analyzeStrength() }

        val state = _formState.value
        val request = CaseStrengthRequest(
            evidence_items = state.evidence.filter { it.isNotBlank() }.size,
            witness_count = state.peopleInvolved.filter { it.isNotBlank() }.size,
            documentary_support = state.evidence.isNotEmpty(),
            police_complaint_filed = false,
            incident_recency_days = 30,
            jurisdiction_match = true,
            user_id = tokenManager.getUserId() ?: "unknown_user"
        )
        
        viewModelScope.launch {
            repository.analyzeStrength(request).collect {
                _strengthState.value = it
            }
        }
    }

    fun generateDraft() {
        if (_draftState.value is Resource.Loading) return
        if (!validateInputs()) return
        
        lastAction = { generateDraft() }

        val state = _formState.value
        val request = DraftRequest(
            draft_type = "legal_draft",
            facts = state.description,
            parties = state.peopleInvolved.filter { it.isNotBlank() },
            relief_sought = "Appropriate legal remedy",
            jurisdiction = state.location,
            user_id = tokenManager.getUserId() ?: "unknown_user"
        )
        
        viewModelScope.launch {
            repository.generateDraft(request).collect {
                _draftState.value = it
            }
        }
    }
    
    fun retryLastAction() {
        _analysisState.value = Resource.Idle()
        _strengthState.value = Resource.Idle()
        _draftState.value = Resource.Idle()
        _formState.value = _formState.value.copy(formError = null)
        lastAction?.invoke()
    }
    
    fun dismissDialogs() {
        _analysisState.value = Resource.Idle()
        _strengthState.value = Resource.Idle()
        _draftState.value = Resource.Idle()
    }
}
