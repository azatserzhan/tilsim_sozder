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
                    Bot(
                        "Бот: Астана",
                        "Астана қаласы бойынша Дұғалық кездесу немесе басқа да проекттердің статистикасын жүргізетін сервис",
                        "https://t.me/Astana_bahai_bot",
                        R.drawable.astana_bot
                    ),
                    Bot("Бот: Алматы",
                        "Алматы қаласы бойынша Дұғалық кездесу немесе басқа да проекттердің статистикасын жүргізетін сервис",
                        "https://t.me/bahai_almaty_bot", R.drawable.almaty_bot),
                    Bot("Telegram: Медиа Канал Бахаи",
                        "",
                        "https://t.me/mediabahai", R.drawable.bot),
                    Bot("Telegram: Цитаты Бахаи",
                        "",
                        "https://t.me/bahaiwisdom", R.drawable.bot)
                )

                view?.showBots(bots)
            }
            SupportLanguage.UZ.code -> {
                val bots = listOf(
                    Bot("Telegram: Медиа Канал Бахаи",
                        "",
                        "https://t.me/mediabahai", R.drawable.bot),
                    Bot("Telegram: Цитаты Бахаи",
                        "",
                        "https://t.me/bahaiwisdom", R.drawable.bot)
                )

                view?.showBots(bots)
            }
        }
    }
}