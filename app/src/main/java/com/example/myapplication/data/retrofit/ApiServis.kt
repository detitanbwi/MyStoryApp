package com.example.myapplication.data.retrofit
import com.example.myapplication.data.response.DetailStoryResponse
import com.example.myapplication.data.response.LoginResponse
import com.example.myapplication.data.response.PostStoryResponse
import com.example.myapplication.data.response.RegisterResponse
import com.example.myapplication.data.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): PostStoryResponse

    @GET("stories")
    fun getStories(
        @Header("Authorization") authorization: String,
        @Query("location") location: Int = 1
    ): Call<StoryListResponse>

    @GET("stories")
    suspend fun getStoriesPaging(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<StoryListResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") authorization: String,
        @Path("id") storyId: String
    ): Call<DetailStoryResponse>
}