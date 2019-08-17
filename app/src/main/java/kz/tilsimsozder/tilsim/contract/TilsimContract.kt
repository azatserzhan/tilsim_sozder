package kz.tilsimsozder.tilsim.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.tilsimsozder.model.Prayer

interface TilsimContract {

    interface View : MvpView {
        fun showPrayers(prayers: List<Prayer>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
    }
}