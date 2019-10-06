package kz.tilsimsozder.settings.contract

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView

interface SettingsContract {

    interface View : MvpView {
        fun showPrayers()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
    }
}