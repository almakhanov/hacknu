package kz.validol.hacknu

import kz.validol.hacknu.entities.Book

object Singleton {
    var allBooks = arrayListOf<Book>()
    var recommendedBooks = arrayListOf<Book>()
}