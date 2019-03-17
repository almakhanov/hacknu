package kz.validol.hacknu.entities

data class CreateBookResponse(
    val book: CreateBookResponseBook,
    val code: Int
)

data class CreateBookResponseBook(
    val author: String,
    val belong: Belong,
    val comments: List<Comment>,
    val company_book: Boolean,
    val description: String,
    val genre: List<Genre>,
    val history: List<History>,
    val id: Int,
    val isbn: String,
    val name: String,
    val photo: String,
    val rating: Int,
    val rating_count: Int,
    val reader: Reader,
    val requesters: List<Any>
)

data class Belong(
    val email: String,
    val id: Int,
    val name: String,
    val password: String,
    val phone: String,
    val photo: String,
    val position: String,
    val read: List<Any>,
    val token: String
)

