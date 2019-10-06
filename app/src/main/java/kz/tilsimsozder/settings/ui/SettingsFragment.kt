package kz.tilsimsozder.settings.ui

import kz.tilsimsozder.common.BaseFragment
import kz.tilsimsozder.settings.contract.SettingsContract
import kz.tilsimsozder.settings.presenter.SettingsPresenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsContract.View, SettingsContract.Presenter>(),
    SettingsContract.View {
    companion object {
        fun create() = SettingsFragment()
    }

    private val presenterImpl: SettingsPresenter by viewModel()
    override val presenter: SettingsContract.Presenter
        get() = presenterImpl

    override fun showPrayers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}