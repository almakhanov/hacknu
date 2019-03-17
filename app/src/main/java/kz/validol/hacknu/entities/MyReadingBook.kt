package kz.validol.hacknu.entities

data class MyReadingBook(
    val book: List<BookReading>
)

data class BookReading(
    val author: String,
    val description: String,
    val genre: List<Genre>,
    val isbn: String,
    val name: String,
    val photo: String,
    val rating: Int,
    val reader: Int
)
