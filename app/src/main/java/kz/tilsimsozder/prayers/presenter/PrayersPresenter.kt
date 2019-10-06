package kz.tilsimsozder.prayers.presenter

import android.annotation.SuppressLint
import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.contract.PrayersContract
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.preference.PreferenceContract
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SharedPreference.Companion.KZ
import kz.tilsimsozder.preference.SharedPreference.Companion.RU
import kz.tilsimsozder.preference.SharedPreference.Companion.UZ

private const val URL_APP = "https://play.google.com/store/apps/details?id=kz.tilsimsozder"

class PrayersPresenter(
    private val analytics: Analytics,
    private val context: Context,
    private val preference: PreferenceContract
) : BasePresenter<PrayersContract.View>(),
    PrayersContract.Presenter {

    private var prayersTitle = listOf<String>()
    private var prayersBody = listOf<String>()
    private var prayers = mutableListOf<Prayer>()
    private var positionPrayer = 0

    override fun loadPrayers() {
        when (preference.getLanguageCode()) {
            KZ -> {
                prayersTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
                prayersBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()
            }
            RU -> {
                prayersTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
                prayersBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()
            }
            UZ -> {
                prayersTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
                prayersBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()
            }
        }

        prayers = prayersTitle
            .map { Prayer(title = it, body = "") }
            .toList()
            .apply {
                this.forEachIndexed { index, tilsimsoz ->
                    tilsimsoz.body = prayersBody[index]
                }
            }
            .toMutableList()

        val favouriteIds = SharedPreference(context).getFavourites()
        favouriteIds?.forEach { id ->
            prayers.firstOrNull { it.id == id }?.isFavourite = true
        }
        prayers.sortBy { !it.isFavourite }

        view?.showPrayers(prayers)
    }

    override fun selectedPrayer(title: String, body: String) {
        view?.showPrayerDialog(title, body)
    }

    override fun setAdapter(prayers: List<Prayer>) {
    }

    override fun sharePrayer() {
        analytics.sharePrayer(prayersTitle[positionPrayer])
        view?.sharePrayer(URL_APP, prayersTitle[positionPrayer], prayersBody[positionPrayer])
    }

    @SuppressLint("DefaultLocale")
    override fun sortPrayer(newText: String) {
        val sorted: List<Prayer> = prayers.filter {
            newText.toLowerCase() in it.title.toLowerCase() || newText.toLowerCase() in it.body.toLowerCase()
        }
        view?.showPrayers(sorted)
    }

    override fun setFavourite(id: String) {
        val isFavorite: Boolean? = prayers.firstOrNull { it.id == id }?.isFavourite

        isFavorite?.let { state ->
            prayers.firstOrNull { it.id == id }?.isFavourite = !state
        }
        prayers.sortBy { !it.isFavourite }

        val favouriteIds = prayers.filter { it.isFavourite }.map { it.id }
        SharedPreference(context).setFavourites(favouriteIds)

        view?.showPrayers(prayers)
        view?.scrollToTop()
    }
}