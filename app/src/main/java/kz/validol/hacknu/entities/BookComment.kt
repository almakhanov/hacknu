package kz.validol.hacknu.entities

data class BookComment(
    var id: Int? = null,
    var text: String? = null,
    var author: User? = null,
    var book: Book? = null
)