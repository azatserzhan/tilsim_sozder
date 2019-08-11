package kz.tilsimsozder.prayers

import kz.azatserzhanov.test.common.BaseFragment
import kz.azatserzhanov.test.currency.contract.PrayersContract
import kz.azatserzhanov.test.currency.presenter.PrayersPresenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrayersFragment :
        BaseFragment<PrayersContract.View,
                PrayersContract.Presenter>(),
        PrayersContract.View {

    companion object {
        fun create() = PrayersFragment()
    }

    private val presenterImpl: PrayersPresenter by viewModel()
    override val presenter: PrayersContract.Presenter
        get() = presenterImpl

    override fun showPrayers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}