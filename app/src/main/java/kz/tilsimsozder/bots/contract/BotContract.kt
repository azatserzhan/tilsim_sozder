package kz.tilsimsozder.bots.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.bots.model.Bot

interface BotContract {

    interface View : MvpView {
        fun showBots(bots: List<Bot>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadBots()
    }
}