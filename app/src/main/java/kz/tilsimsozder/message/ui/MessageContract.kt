package kz.tilsimsozder.message.ui

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView

interface MessageContract {

    interface View : MvpView {
        fun show()
    }

    interface Presenter : MvpPresenter<View> {
        fun load()
    }
}