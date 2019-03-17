package kz.validol.hacknu.entities

data class CommunityResponse(
    val author: Author,
    val book: BookCommunity,
    val id: Int
)

data class Author(
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

data class BookCommunity(
    val author: String,
    val belong: Any,
    val comments: List<Any>,
    val company_book: Boolean,
    val description: String,
    val genre: List<Genre>,
    val history: List<Any>,
    val id: Int,
    val isbn: String,
    val name: String,
    val photo: String,
    val rating: Int,
    val rating_count: Int,
    val reader: Any,
    val requesters: List<Any>
)

data class Genre(
    val name: String
)