package kz.validol.hacknu.auth

import android.annotation.SuppressLint
import kz.validol.hacknu.core.util.BasePresenter

class LoginPresenter(private val repository: LoginContract.LoginRepository): LoginContract.LoginPresenter,
                    BasePresenter<LoginContract.LoginView>(){

    @SuppressLint("CheckResult")
    override fun signIn(email:String, password:String) {
        repository.login(email,password)
                .doOnSubscribe { getView()?.showProgress() }
                .doOnComplete { getView()?.hideProgress() }
                .subscribe({
                    if (it.code == 0) {
                        getView()?.success(it.user!!)
                    }
                    else{
                        getView()?.showError("Неправильный пароль или логин")
                    }
                },{
                    getView()?.showError("Нет соединение с сервером")
                })
    }

    override fun viewIsReady() {

    }

    override fun destroy() {

    }

}