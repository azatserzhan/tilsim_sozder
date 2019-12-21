package kz.tilsimsozder.news.presenter

import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.contract.NewsContract
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage

class NewsPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<NewsContract.View>(),
    NewsContract.Presenter {

    override fun loadNews() {
        when (SharedPreference(context).getLanguageCode()) {
            SupportLanguage.KZ.code, SupportLanguage.RU.code -> {
                val bots = listOf(
                    Bot(
                        R.string.news_kz_oficial_site_title,
                        R.string.news_kz_oficial_site,
                        "http://bahai.kz/?page_id=11&lang=ru",
                        R.drawable.ic_bahai_kz
                    ),
                    Bot(R.string.news_worl_bahai_title,
                        R.string.news_worl_bahai,
                        "https://www.bahai.org/ru/",
                        R.drawable.ic_bahai_org
                    ),
                    Bot(R.string.film_bab_title,
                        R.string.film_bab_description,
                        "https://bicentenary.bahai.org/ru/the-bab/",
                        R.drawable.ic_bab
                    ),
                    Bot(R.string.youtube_kz_bahai,
                        url = "https://www.youtube.com/channel/UCSOVNuKVx_HovSbTpRZnt3Q",
                        imageRes = R.drawable.youtube_bahai_channel
                    ),
                    Bot(R.string.kz_instagram_title,
                        url = "https://www.instagram.com/kazakhstan_bahai/",
                        imageRes = R.drawable.insta_bahai_channel)
                )

                view?.showNews(bots)
            }
            SupportLanguage.UZ.code -> {
                val bots = listOf(
                    Bot(
                        R.string.news_uz_bahai,
                        R.string.news_uz_bahai_description,
                        "https://baxoi.uz/uzl/",
                        R.drawable.ic_bahai_kz
                    ),
                    Bot(R.string.news_worl_bahai_title,
                        R.string.news_worl_bahai,
                        "https://www.bahai.org/ru/",
                        R.drawable.ic_bahai_org
                    ),
                    Bot(R.string.film_bab_title,
                        R.string.film_bab_description,
                        "https://bicentenary.bahai.org/ru/the-bab/",
                        R.drawable.ic_bab
                    )
                )

                view?.showNews(bots)
            }
        }
    }
}