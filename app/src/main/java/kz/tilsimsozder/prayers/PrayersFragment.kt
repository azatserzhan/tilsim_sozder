package kz.tilsimsozder.prayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.azatserzhanov.test.common.BaseFragment
import kz.azatserzhanov.test.currency.contract.PrayersContract
import kz.azatserzhanov.test.currency.presenter.PrayersPresenter
import kz.tilsimsozder.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_prayers, container, false)

    override fun showPrayers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}