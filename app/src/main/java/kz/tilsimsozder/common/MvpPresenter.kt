package kz.azatserzhanov.test.common

interface MvpPresenter<V : MvpView> {

    fun attach(view: V)

    fun detach()
}