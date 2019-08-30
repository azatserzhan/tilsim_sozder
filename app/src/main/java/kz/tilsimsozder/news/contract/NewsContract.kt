package kz.tilsimsozder.news.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView

interface NewsContract {

    interface View : MvpView {
        fun showNews(url: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadNews()
    }
}