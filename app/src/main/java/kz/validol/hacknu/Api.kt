package kz.validol.hacknu

import io.reactivex.Observable
import kz.validol.hacknu.entities.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @POST("authorize/")
    fun register(@Body user: User): Observable<LoginResponse>

    @GET("auth/")
    fun login(
        @Query("phone") login: String,
        @Query("password") password: String,
        @Query("token") token: String,
        @Query("email") username: String
    ): Observable<LoginResponse>

    @GET("books/")
    fun getBooks(): Observable<List<Book>>

    @GET("my_books/")
    fun getMyBooks(): Observable<List<Book>>

    @GET("getGenreBooks/")
    fun getGenreBooks(@Query("genre") genre: String): Observable<List<Book>>

    @GET("get_book_info/")
    fun getBookByISBN(@Query("isbn") isbn: String): Observable<BookResponse>

    @GET("book/add_comment/")
    fun addComment(@Query("text") text: String,
                   @Query("book_id") bookID: Int,
                   @Query("author_id")userID: Int): Observable<BookCommentResponse>

    @GET("book/change_reader/")
    fun requestOwner(@Query("consumer_id") user_id: Int?,
                     @Query("isbn") book_isbn: String?): Observable<GeneralRespose>
}