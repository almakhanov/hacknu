package kz.validol.hacknu.entities

import kz.validol.hacknu.home.GenreItem

data class Book(
    var id: Int? = null,
    var name: String? = null,
    var isbn: String? = null,
    var author: String? = null,
    var description: String? = null,
    var photo: String? = null,
    var company_book: Boolean? = false,
    var genre: List<GenreItem>? = null,
    var comments: List<BookComment>? = null,
    var belong: User? = null,
    var reader: User? = null,
    var history: List<User>? = null,
    var rating: Float? = null,
    var requesters: List<User>? = null
)

//data class NewBook(
//    val book: Book,
//    val code: Int
//)
//
//data class Book(
//    val author: String,
//    val belong: Any,
//    val comments: List<Comment>,
//    val company_book: Boolean,
//    val description: String,
//    val genre: List<Genre>,
//    val history: List<Any>,
//    val id: Int,
//    val isbn: String,
//    val name: String,
//    val photo: String,
//    val rating: Int,
//    val rating_count: Int,
//    val reader: Reader,
//    val requesters: List<Any>
//)
//
//data class Reader(
//    val email: String,
//    val id: Int,
//    val name: String,
//    val password: String,
//    val phone: String,
//    val photo: String,
//    val position: String,
//    val read: List<Any>,
//    val token: String
//)
//
//data class Genre(
//    val name: String
//)
//
//data class Comment(
//    val author: Author,
//    val date: String,
//    val id: Int,
//    val text: String
//)
//
//data class Author(
//    val email: String,
//    val id: Int,
//    val name: String,
//    val password: String,
//    val phone: String,
//    val photo: String,
//    val position: String,
//    val read: List<Any>,
//    val token: String
//)
