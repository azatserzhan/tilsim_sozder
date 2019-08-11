package kz.azatserzhanov.test.currency.presenter

import kz.azatserzhanov.test.common.BasePresenter
import kz.azatserzhanov.test.currency.contract.PrayersContract

class PrayersPresenter : BasePresenter<PrayersContract.View>(),
        PrayersContract.Presenter {

    override fun loadPrayers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}