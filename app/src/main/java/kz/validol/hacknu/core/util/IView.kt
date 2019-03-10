package kz.validol.hacknu.core.util

interface IView<out P : IPresenter<*>> {
    val presenter: P
}