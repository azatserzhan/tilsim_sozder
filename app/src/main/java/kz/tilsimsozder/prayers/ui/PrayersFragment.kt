package kz.tilsimsozder.prayers.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_prayers.*
import kotlinx.android.synthetic.main.fragment_prayers.TextViewHeader
import kotlinx.android.synthetic.main.fragment_prayers.bodyTextView
import kotlinx.android.synthetic.main.fragment_prayers.prayerListRecyclerView
import kotlinx.android.synthetic.main.fragment_prayers.shareImageView
import kz.tilsimsozder.common.BaseFragment
import kz.tilsimsozder.R
import kz.tilsimsozder.common.BaseBottomSheetDialog
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.adapter.PrayerAdapter
import kz.tilsimsozder.prayers.contract.PrayersContract
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.prayers.presenter.PrayersPresenter
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.tilsim.ui.TilsimDialogFragment
import kz.tilsimsozder.tilsim.ui.TilsimDialogFragment.Companion.TILSIM_DIALOG_FRAGMENT
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
                textClickListener = {title, body ->
                    presenter.selectedPrayer(title, body)
                },
                favouriteClickListener = {

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

        bodyTextView.setOnClickListener {
            navigationFrameLayout.isVisible = !navigationFrameLayout.isVisible
            bottomMenuButton.isVisible = !bottomMenuButton.isVisible
            seekBarMain.isVisible = !seekBarMain.isVisible
        }

        /*new*/
        searchPrayer.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("azat", query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("azat", newText)
                presenter.sortPrayer(newText)
                return false
            }
        })

        searchPrayer.setOnClickListener{
            searchPrayer.onActionViewExpanded()
        }
    }

    override fun showPrayers(prayers: List<Prayer>) {
        prayerAdapter?.addItems(prayers)
    }

    @SuppressLint("DefaultLocale")
    override fun updatePrayer(title: String, body: String) {
        TextViewHeader.text = title.toUpperCase()
        bodyTextView.text = body
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
        bodyTextView.text = body
    }

    override fun showPrayerDialog(title: String, body: String) {
        val bottomSheetDialogFragment =
                BaseBottomSheetDialog.create(TilsimDialogFragment.create(
                        title,
                        body
                ))
        bottomSheetDialogFragment.show(childFragmentManager, TILSIM_DIALOG_FRAGMENT)
    }
}