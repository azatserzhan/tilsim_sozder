package kz.tilsimsozder.news.contract

import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView

interface NewsContract {

    interface View : MvpView {
        fun showNews(bots: List<Bot>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadNews()
    }
}