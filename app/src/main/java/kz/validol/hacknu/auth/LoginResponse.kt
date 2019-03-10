package kz.validol.hacknu.auth

import com.google.gson.annotations.SerializedName
import kz.validol.hacknu.local_storage.Customer

/**
 * Data class for Token
 */


data class LoginResponse(
        @SerializedName("code") val code: Int,
        @SerializedName("user") val user: Customer?
)