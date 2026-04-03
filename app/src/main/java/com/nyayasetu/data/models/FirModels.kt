package com.nyayasetu.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FirRequest(
    val complainant_name: String,
    val parent_name: String,
    val address: String,
    val contact_number: String,
    val police_station: String,
    val incident_date: String,
    val incident_time: String,
    val incident_location: String,
    val incident_description: String,
    val accused_details: List<String> = emptyList(),
    val witness_details: List<String> = emptyList(),
    val evidence_information: List<String> = emptyList(),
    val draft_role: String = "citizen_application",
    val language: String = "en",
    val user_id: String
)

@Serializable
data class FirPreviewResponse(
    val preview_text: String? = null,
    val generated_draft: String? = null,
    val message: String? = null,
    val data: JsonElement? = null
)

@Serializable
data class FirCreateResponse(
    val fir_id: String? = null,
    val status: String? = null,
    val message: String? = null,
    val data: JsonElement? = null
)
