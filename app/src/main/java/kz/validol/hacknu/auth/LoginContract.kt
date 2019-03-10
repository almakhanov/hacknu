package kz.validol.hacknu.auth

import io.reactivex.Observable
import kz.validol.hacknu.core.util.IPresenter
import kz.validol.hacknu.core.util.IView
import kz.validol.hacknu.local_storage.Customer

interface LoginContract{

    interface LoginView: IView<LoginPresenter> {
        fun showProgress()
        fun hideProgress()
        fun success(user: Customer)
        fun showError(text: String)
    }

    interface LoginPresenter: IPresenter<LoginView> {
        fun signIn(email:String, password:String)

    }

    interface LoginRepository{
        fun login(email:String,password:String):Observable<LoginResponse>
    }
}