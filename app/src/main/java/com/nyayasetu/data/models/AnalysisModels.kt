package com.nyayasetu.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CaseAnalysisRequest(
    val incident_description: String,
    val location: String,
    val incident_date: String,
    val people_involved: List<String> = emptyList(),
    val evidence: List<String> = emptyList(),
    val language: String = "en",
    val user_id: String
)

@Serializable
data class CaseStrengthRequest(
    val evidence_items: Int,
    val witness_count: Int,
    val documentary_support: Boolean,
    val police_complaint_filed: Boolean,
    val incident_recency_days: Int,
    val jurisdiction_match: Boolean,
    val user_id: String
)

@Serializable
data class DraftRequest(
    val draft_type: String,
    val facts: String,
    val parties: List<String> = emptyList(),
    val relief_sought: String,
    val jurisdiction: String,
    val user_id: String
)

@Serializable
data class FirAnalysisRequest(
    val incident_description: String,
    val location: String,
    val incident_date: String,
    val people_involved: List<String> = emptyList(),
    val evidence: List<String> = emptyList(),
    val language: String = "en",
    val user_id: String
)

@Serializable
data class AnalysisResponse(
    val status: String? = null,
    val message: String? = null,
    val data: JsonElement? = null,
    val summary: String? = null,
    val score: Int? = null,
    val verdict: String? = null,
    val draft_text: String? = null,
    val rationale: List<String>? = null,
    val applicable_laws: List<String>? = null,
    val legal_reasoning: String? = null,
    val possible_punishment: String? = null,
    val recommended_steps: List<String>? = null
)
