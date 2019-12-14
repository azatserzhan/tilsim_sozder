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
        when(SharedPreference(context).getLanguageCode()) {
            SupportLanguage.KZ.code, SupportLanguage.RU.code -> {
                val bots = listOf(
                    Bot(
                        "www.bahai.kz",
                        context.getString(R.string.news_kz_oficial_site),
                        "http://bahai.kz/?page_id=13&lang=kk",
                        R.drawable.ic_bahai_kz
                    ),
                    Bot("www.bahai.org/ru/",
                        context.getString(R.string.news_worl_bahai),
                        "https://www.bahai.org/ru/",
                        R.drawable.ic_bahai_org
                    ),
                    Bot("Восхождение света",
                        context.getString(R.string.film_bab_description),
                        "https://bicentenary.bahai.org/ru/the-bab/",
                        R.drawable.ic_bab
                    ),
                    Bot(context.getString(R.string.youtube_kz_bahai),
                        "",
                        "https://www.youtube.com/channel/UCSOVNuKVx_HovSbTpRZnt3Q", R.drawable.youtube_bahai_channel),
                    Bot("Instagram: kazakhstan_bahai",
                        "",
                        "https://www.instagram.com/kazakhstan_bahai/", R.drawable.insta_bahai_channel)
                )

                view?.showNews(bots)
            }
            SupportLanguage.UZ.code -> {
                val bots = listOf(
                    Bot(
                        "baxoi.uz",
                        "O'zbekiston Bahoiy jamoasining rasmiy sayti",
                        "https://baxoi.uz/uzl/",
                        R.drawable.ic_bahai_kz
                    ),
                    Bot("www.bahai.org",
                        context.getString(R.string.news_worl_bahai),
                        "https://www.bahai.org/ru/",
                        R.drawable.ic_bahai_org
                    ),
                    Bot("Восхождение света",
                        "Cпециально подготовленный фильм к двухсотлетию",
                        "https://bicentenary.bahai.org/ru/the-bab/",
                        R.drawable.ic_bab
                    )
                )

                view?.showNews(bots)
            }
        }

    }
}