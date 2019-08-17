package kz.tilsimsozder.tilsim.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.tilsim.model.Tilsim
import kz.tilsimsozder.tilsimsozder.model.Prayer

interface TilsimContract {

    interface View : MvpView {
        fun showTilsim(tilsimList: List<Tilsim>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadTilsim()
    }
}