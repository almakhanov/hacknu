package kz.validol.hacknu.auth

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRepository(private val remoteService:LoginService):LoginContract.LoginRepository {
    override fun login(email: String, password: String):Observable<LoginResponse> {
        return remoteService.authorize(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}