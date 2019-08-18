package kz.tilsimsozder.news.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.tilsimsozder.model.Prayer

interface NewsContract {

    interface View : MvpView {
        fun showNews(url: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadNews()
    }
}