package kz.azatserzhanov.test.currency.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView

interface PrayersContract {

    interface View : MvpView {
        fun showPrayers()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
    }
}