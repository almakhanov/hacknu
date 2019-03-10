package kz.validol.hacknu.entities

import com.google.gson.annotations.SerializedName
import kz.validol.hacknu.entities.User

data class LoginResponse(
        @SerializedName("code") val code: Int,
        @SerializedName("user") val user: User?
)