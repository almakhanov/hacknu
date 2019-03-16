package kz.validol.hacknu.entities

data class User(
    var id: Int? = null,
    var phone: String? = null,
    var email: String? = null,
    var name: String? = null,
    var password: String? = null,
    var age: Int? = null,
    var photo: String? = null,
    var token: String? = null,
    var position: String? = null
)