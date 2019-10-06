package kz.tilsimsozder.settings.presenter

import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.settings.contract.SettingsContract

class SettingsPresenter(
    private val preference: SharedPreference
) : BasePresenter<SettingsContract.View>(),
    SettingsContract.Presenter {

    override fun loadPrayers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}