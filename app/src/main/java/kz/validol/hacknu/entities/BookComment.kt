package kz.validol.hacknu.entities

data class BookComment(
    val author: User?,
    val date: String?,
    val id: Int?,
    val text: String?
)