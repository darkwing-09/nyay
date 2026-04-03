package com.nyayasetu.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SectionPredictionRequest(
    val incident_description: String
)

@Serializable
data class JurisdictionRequest(
    val incident_location: String
)

@Serializable
data class AdvancedFirResponse(
    val status: String? = null,
    val message: String? = null,
    val data: JsonElement? = null,
    val sections: List<String>? = null,
    val jurisdiction: String? = null,
    val completeness_score: Int? = null,
    val analysis: String? = null
)
