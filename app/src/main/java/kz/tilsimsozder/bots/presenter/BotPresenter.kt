package kz.tilsimsozder.bots.presenter

import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.contract.BotContract
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics

class BotPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<BotContract.View>(),
        BotContract.Presenter {

    override fun loadBots() {
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
}