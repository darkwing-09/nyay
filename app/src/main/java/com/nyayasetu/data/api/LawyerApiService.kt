package com.nyayasetu.data.api

import com.nyayasetu.data.models.*
import retrofit2.http.*

interface LawyerApiService {
    @GET("/api/v1/lawyers")
    suspend fun getLawyers(): List<Lawyer>

    @GET("/api/v1/lawyers/{handle}")
    suspend fun getLawyerProfile(@Path("handle") handle: String): LawyerProfile

    @POST("/api/v1/lawyers/{handle}/follow")
    suspend fun followLawyer(@Path("handle") handle: String): GenericLawyerResponse

    @GET("/api/v1/lawyers/network/feed")
    suspend fun getFeed(): List<FeedPost>

    @POST("/api/v1/lawyers/network/posts")
    suspend fun createPost(@Body request: CreatePostRequest): GenericLawyerResponse

    @POST("/api/v1/lawyers/network/posts/{post_id}/like")
    suspend fun likePost(@Path("post_id") postId: String): GenericLawyerResponse
}
