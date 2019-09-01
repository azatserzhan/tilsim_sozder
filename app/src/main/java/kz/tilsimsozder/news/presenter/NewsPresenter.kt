package kz.tilsimsozder.news.presenter

import android.content.Context
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.contract.NewsContract

private const val URL_NEWS = "http://bahai.kz/?page_id=19&lang=kk"

class NewsPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<NewsContract.View>(),
        NewsContract.Presenter {

    override fun loadNews() {
        view?.showNews(URL_NEWS)
    }
}