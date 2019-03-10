package kz.validol.hacknu.auth

import kz.validol.hacknu.core.createService
import org.koin.dsl.module.module

val authModule = module {
    factory { LoginPresenter(get()) as LoginContract.LoginPresenter }
    factory { LoginRepository(get()) as LoginContract.LoginRepository }
    single { createService<LoginService>(get()) }
}