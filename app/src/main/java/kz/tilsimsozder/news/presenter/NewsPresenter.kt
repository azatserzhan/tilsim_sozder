package kz.tilsimsozder.news.presenter

import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.contract.NewsContract

private const val URL_NEWS = "http://bahai.kz/?page_id=19&lang=kk"

class NewsPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<NewsContract.View>(),
        NewsContract.Presenter {

    override fun loadNews() {
        val bots = listOf(
            Bot(
                "www.bahai.kz",
                "Қазақстан бахаиларының ресими сайтынан жаңалықтар",
                "http://www.bahai.kz",
                R.drawable.ic_bahai_kz
            ),
            Bot("www.bahai.org",
                "Бүкл әлемдегі бахаиларының ресми сайты",
                "https:www.bahai.org",
                R.drawable.ic_bahai_org
            ),
            Bot("Восхождение света",
                "Баб туралы бейне фильм.\n" +
                    "Орыс тілінде",
                "https://bicentenary.bahai.org/ru/the-bab/",
                R.drawable.ic_bab
            )
        )

        view?.showNews(bots)
    }
}