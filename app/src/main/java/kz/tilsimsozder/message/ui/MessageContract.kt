package kz.tilsimsozder.message.ui

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView
import kz.tilsimsozder.prayers.model.Prayer

interface MessageContract {

    interface View : MvpView {
        fun showPrayers(prayers: List<Prayer>)
        fun sharePrayer(urlApp: String, title: String, body: String)
        fun showPrayerDialog(title: String, body: String, url: String)
        fun scrollToTop()
        fun showLanguageDialog()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
        fun selectedPrayer(title: String, body: String, url: String)
        fun setAdapter(prayers: List<Prayer>)
        fun sharePrayer()
        fun sortPrayer(newText: String)
        fun setFavourite(id: String)
        fun checkLanguage()
    }
}