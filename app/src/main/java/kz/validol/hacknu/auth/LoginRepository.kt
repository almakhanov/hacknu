package kz.validol.hacknu.auth

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.validol.hacknu.Api

class LoginRepository(private val api: Api) : LoginContract.LoginRepository {
    override fun login(email: String, password: String): Observable<LoginResponse> {
        return api.authorize(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}