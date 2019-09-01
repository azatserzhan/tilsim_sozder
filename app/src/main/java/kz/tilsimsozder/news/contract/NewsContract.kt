package kz.tilsimsozder.news.contract

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView

interface NewsContract {

    interface View : MvpView {
        fun showNews(url: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadNews()
    }
}