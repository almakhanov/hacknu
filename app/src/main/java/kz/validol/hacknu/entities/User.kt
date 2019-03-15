package kz.validol.hacknu.entities

data class User(
    var id: Int? = null,
    var email: String? = null,
    var name: String? = null,
    var password: String? = null,
    var age: Int? = null,
    var photo: String? = null,
    var FCMToken: String? = null
)