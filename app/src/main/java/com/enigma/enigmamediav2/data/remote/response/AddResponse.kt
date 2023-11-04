package com.enigma.enigmamediav2.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
