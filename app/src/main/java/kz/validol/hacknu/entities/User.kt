package kz.validol.hacknu.entities

data class User(
    var id: Int?,
    var email: String?,
    var name: String?,
    var password: String?,
    var age: Int?,
    var photo: String?,
    var FCMToken: String?
)