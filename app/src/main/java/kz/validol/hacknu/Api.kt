package kz.validol.hacknu

import io.reactivex.Observable
import kz.validol.hacknu.auth.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("login/")
    fun authorize(@Query("email") email:String,
                  @Query("password") password:String): Observable<LoginResponse>
}