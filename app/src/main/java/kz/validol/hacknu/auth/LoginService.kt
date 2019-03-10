package kz.validol.hacknu.auth

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService{
    @GET("login/")
    fun authorize(@Query("email") email:String,
                  @Query("password") password:String): Observable<LoginResponse>
}