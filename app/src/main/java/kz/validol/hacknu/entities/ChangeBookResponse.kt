package kz.validol.hacknu.entities

data class ChangeBookResponse(
    val book: ChangeBook,
    val code: Int
)

data class ChangeBook(
    val author: String,
    val belong: Any,
    val comments: List<Comment>,
    val company_book: Boolean,
    val description: String,
    val genre: List<ChangeGenre>,
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

data class Reader(
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

data class History(
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

data class ChangeGenre(
    val name: String
)

data class Comment(
    val author: ChangeAuthor,
    val date: String,
    val id: Int,
    val text: String
)

data class ChangeAuthor(
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