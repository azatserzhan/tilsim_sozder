package kz.tilsimsozder.prayers.presenter

import android.annotation.SuppressLint
import android.content.Context
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.api.PrayersApi
import kz.tilsimsozder.prayers.contract.PrayersContract
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.preference.SharedPreference

private const val URL_APP = "https://play.google.com/store/apps/details?id=kz.tilsimsozder"

class PrayersPresenter(
    private val analytics: Analytics,
    private val context: Context,
    private val prayersApi: PrayersApi
) : BasePresenter<PrayersContract.View>(),
    PrayersContract.Presenter {

    private var prayers = mutableListOf<Prayer>()
    private var positionPrayer = 0

    override fun loadPrayers() {
        prayers.clear()
        prayers.addAll(prayersApi.getPrayers())

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
        analytics.sharePrayer(prayers[positionPrayer].title)
        view?.sharePrayer(URL_APP, prayers[positionPrayer].title, prayers[positionPrayer].body)
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

    override fun checkLanguage() {
        if (!SharedPreference(context).getIsLanguageDialogShow()) {
            view?.showLanguageDialog()
            SharedPreference(context).setLanguageDialogShow(true)
        }
    }
}