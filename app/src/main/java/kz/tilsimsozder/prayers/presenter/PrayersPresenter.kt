package kz.tilsimsozder.prayers.presenter

import android.content.Context
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.contract.PrayersContract
import kz.tilsimsozder.prayers.model.Prayer

private const val URL_APP = "https://play.google.com/store/apps/details?id=kz.tilsimsozder"

class PrayersPresenter(private val analytics: Analytics, val context: Context) : BasePresenter<PrayersContract.View>(),
        PrayersContract.Presenter {

    private var prayersTitle = listOf<String>()
    private var prayersBody = listOf<String>()
    private var positionPrayer = 0

    override fun loadPrayers() {
        prayersTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
        prayersBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()

        val prayers = prayersTitle
                .map { Prayer(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.content = prayersBody[index]
                    }
                }
        view?.showPrayers(prayers)
    }

    override fun selectedPrayer(position: Int) {
        view?.showPrayerDialog(prayersTitle[position], prayersBody[position])
        //view?.updatePrayer(prayersTitle[position], prayersBody[position])
        //analytics.showPrayer(prayersTitle[position])
        positionPrayer = position
    }

    override fun setAdapter(prayers: List<Prayer>) {

    }

    override fun sharePrayer() {
        analytics.sharePrayer(prayersTitle[positionPrayer])
        view?.sharePrayer(URL_APP, prayersTitle[positionPrayer], prayersBody[positionPrayer])
    }

    override fun nextPrayer() {
        if (positionPrayer < prayersBody.size - 1) {
            positionPrayer++
        }

        view?.showPrayer(prayersTitle[positionPrayer], prayersBody[positionPrayer])
        analytics.showPrayer(prayersTitle[positionPrayer])
    }

    override fun prevPrayer() {
        if (positionPrayer > 0) {
            positionPrayer--
        }

        view?.showPrayer(prayersTitle[positionPrayer], prayersBody[positionPrayer])
        analytics.showPrayer(prayersTitle[positionPrayer])
    }

}