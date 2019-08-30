package kz.tilsimsozder.prayers.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_prayers.TextViewContent
import kotlinx.android.synthetic.main.fragment_prayers.TextViewHeader
import kotlinx.android.synthetic.main.fragment_prayers.prayerListRecyclerView
import kotlinx.android.synthetic.main.fragment_prayers.shareImageView
import kotlinx.android.synthetic.main.fragment_prayers.slidingDrawer
import kz.azatserzhanov.test.common.BaseFragment
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.adapter.PrayerAdapter
import kz.tilsimsozder.prayers.contract.PrayersContract
import kz.tilsimsozder.prayers.presenter.PrayersPresenter
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.tilsimsozder.model.Prayer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { SharedPreference(it).setCurrentFragmentName(FragmentName.PRAYER.ordinal) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        presenter.loadPrayers()
    }

    private fun setupView() {
        context?.let { analytics.setup(it) }


        prayerAdapter = PrayerAdapter(
                clickListener = {
                    selectPrayer(it)
                })

        val selectPrayerManager = LinearLayoutManager(context)
        prayerListRecyclerView.apply {
            layoutManager = selectPrayerManager
            adapter = prayerAdapter
        }

        shareImageView.setOnClickListener {
            presenter.sharePrayer()
        }

        prayerNextImageButton.setOnClickListener {
            presenter.nextPrayer()
        }

        prayerBackImageButton.setOnClickListener {
            presenter.prevPrayer()
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

    override fun sharePrayer(urlApp: String, title: String, body: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$urlApp \n\n $title \n $body")
        shareIntent.type = "text/plain"
        context?.startActivity(shareIntent)
    }

    override fun showPrayer(title: String, body: String) {
        TextViewHeader.text = title
        TextViewContent.text = body
    }
}