package com.example.myapplication.data.response

import com.google.gson.annotations.SerializedName

data class PostStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
