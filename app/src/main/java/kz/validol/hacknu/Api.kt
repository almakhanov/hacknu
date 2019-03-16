package kz.validol.hacknu

import io.reactivex.Observable
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.entities.LoginResponse
import kz.validol.hacknu.entities.User
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
}