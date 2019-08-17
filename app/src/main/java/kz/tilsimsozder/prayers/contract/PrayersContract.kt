package kz.azatserzhanov.test.currency.contract

import android.content.Context
import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.tilsimsozder.model.Prayer

interface PrayersContract {

    interface View : MvpView {
        fun showPrayers(prayers: List<Prayer>)
        fun updatePrayer(title: String, body: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadPrayers()
        fun selectedPrayer(position: Int)
        fun setAdapter(prayers: List<Prayer>)
    }
}