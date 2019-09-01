package kz.tilsimsozder.bots.contract

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView
import kz.tilsimsozder.bots.model.Bot

interface BotContract {

    interface View : MvpView {
        fun showBots(bots: List<Bot>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadBots()
    }
}