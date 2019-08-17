package kz.tilsimsozder.prayers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_prayers.*
import kz.azatserzhanov.test.common.BaseFragment
import kz.azatserzhanov.test.currency.contract.PrayersContract
import kz.azatserzhanov.test.currency.presenter.PrayersPresenter
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.adapter.PrayerAdapter
import kz.tilsimsozder.tilsimsozder.model.Prayer
import kz.tilsimsozder.tilsimsozder.ui.TilsimsozderFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrayersFragment : BaseFragment<PrayersContract.View, PrayersContract.Presenter>(),
        PrayersContract.View {

    companion object {
        fun create() = PrayersFragment()
    }

    private val presenterImpl: PrayersPresenter by viewModel()
    override val presenter: PrayersContract.Presenter
        get() = presenterImpl
    private var prayerAdapter: PrayerAdapter? = null
    private val analytics = Analytics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_prayers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        presenter.loadPrayers()
    }

    private fun setupView() {
        context?.let {
            analytics.setup(it)
        }


        prayerAdapter = PrayerAdapter(
                clickListener = {
                    selectPrayer(it)
                })

        val selectPrayerManager = LinearLayoutManager(context)
        prayerListRecyclerView.apply {
            layoutManager = selectPrayerManager
            adapter = prayerAdapter
        }
    }

    private fun selectPrayer(position: Int) {
        presenter.selectedPrayer(position)
        slidingDrawer.animateClose()
        // setNotes()
    }

    override fun showPrayers(prayers: List<Prayer>) {
        prayerAdapter?.addItems(prayers)
    }

    override fun updatePrayer(title: String, body: String) {
        TextViewHeader.text = title.toUpperCase()
        TextViewContent.text = body
    }
}