package kz.tilsimsozder.bots.presenter

import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.contract.BotContract
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage

class BotPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<BotContract.View>(),
    BotContract.Presenter {

    override fun loadBots() {
        when (SharedPreference(context).getLanguageCode()) {
            SupportLanguage.KZ.code, SupportLanguage.RU.code -> {
                val bots = listOf(
                    Bot(R.string.calendar_title,
                        R.string.calendar_description,
                        "https://calendar.google.com/calendar?cid=cjRiMnFjOXBvZ3U5bXY1OGE2Z25hYTB2ZG9AZ3JvdXAuY2FsZW5kYXIuZ29vZ2xlLmNvbQ",
                        R.drawable.calendar
                    ),
                    Bot(R.string.bot_astana_name,
                        R.string.bot_ovd_description,
                        "https://t.me/Astana_bahai_bot",
                        R.drawable.astana_bot
                    ),
                    Bot(R.string.bot_almaty_name,
                        R.string.bot_ovd_description,
                        "https://t.me/bahai_almaty_bot", R.drawable.almaty_bot),
                    Bot(R.string.bot_ovd_name,
                        R.string.bot_ovd_description,
                        "https://t.me/bahai_kz_bot",
                        R.drawable.icon
                    ),
                    Bot(R.string.bot_telegram_media_channel_bahai,
                        url = "https://t.me/mediabahai",
                        imageRes = R.drawable.bot),
                    Bot(R.string.bot_telegram_words_bahai,
                        url = "https://t.me/bahaiwisdom",
                        imageRes = R.drawable.bot)
                )

                view?.showBots(bots)
            }
            SupportLanguage.UZ.code -> {
                val bots = listOf(
                    Bot(R.string.bot_telegram_media_channel_bahai,
                        url = "https://t.me/mediabahai",
                        imageRes = R.drawable.bot
                    ),
                    Bot(R.string.bot_telegram_words_bahai,
                        url = "https://t.me/bahaiwisdom",
                        imageRes = R.drawable.bot
                    ))

                view?.showBots(bots)
            }
        }
    }
}