package kz.tilsimsozder.prayers.contract

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView
import kz.tilsimsozder.prayers.model.Prayer

interface PrayersContract {

    interface View : MvpView {
        fun showPrayers(prayers: List<Prayer>)
        fun updatePrayer(title: String, body: String)
        fun sharePrayer(urlApp: String, title: String, body: String)
        fun showPrayer(title: String, body: String)
        fun showPrayerDialog(title: String, body: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
        fun selectedPrayer(title: String, body: String)
        fun setAdapter(prayers: List<Prayer>)
        fun sharePrayer()
        fun nextPrayer()
        fun prevPrayer()
        fun sortPrayer(newText: String)
        fun setFavourite(id: String)
    }
}