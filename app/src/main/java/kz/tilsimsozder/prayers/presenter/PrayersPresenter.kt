package kz.azatserzhanov.test.currency.presenter

import android.content.Context
import kz.azatserzhanov.test.common.BasePresenter
import kz.azatserzhanov.test.currency.contract.PrayersContract
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.tilsimsozder.model.Prayer

class PrayersPresenter(private val analytics: Analytics) : BasePresenter<PrayersContract.View>(),
        PrayersContract.Presenter {

    private var prayersTitle = listOf<String>()
    private var prayersBody = listOf<String>()

    override fun loadPrayers(context: Context) {
        prayersTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
        prayersBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()
    }

    override fun selectedPrayer(position: Int) {
        view?.updatePrayer(prayersTitle[position], prayersBody[position])
        analytics.showPrayer(prayersTitle[position])
    }

    override fun setAdapter(prayers: List<Prayer>) {
        val prayerList = prayersTitle
                .map { Prayer(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.content = prayersBody[index]
                    }
                }

        view?.showPrayers(prayers)
    }

}