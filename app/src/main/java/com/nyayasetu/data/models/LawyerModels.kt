package com.nyayasetu.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Lawyer(
    val id: String = "",
    val handle: String = "",
    val full_name: String = "",
    val bio: String = "",
    val specialization: String = ""
)

@Serializable
data class LawyerProfile(
    val lawyer: Lawyer = Lawyer(),
    val is_following: Boolean = false,
    val follower_count: Int = 0,
    val consulting_fee: Double = 0.0
)

@Serializable
data class FeedPost(
    val post_id: String = "",
    val lawyer_handle: String = "",
    val lawyer_name: String = "",
    val content: String = "",
    val timestamp: String = "",
    val likes_count: Int = 0,
    val is_liked: Boolean = false
)

@Serializable
data class CreatePostRequest(
    val content: String
)

@Serializable
data class GenericLawyerResponse(
    val status: String? = null,
    val message: String? = null,
    val data: JsonElement? = null
)
