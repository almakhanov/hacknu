package kz.validol.hacknu

import io.reactivex.Observable
import kz.validol.hacknu.entities.LoginResponse
import kz.validol.hacknu.entities.User
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("register/")
    fun register(@Body user: User): Observable<LoginResponse>
}