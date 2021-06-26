package kz.tilsimsozder.message.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_message.prayerListRecyclerView
import kotlinx.android.synthetic.main.fragment_message.searchPrayer
import kz.tilsimsozder.R
import kz.tilsimsozder.MainActivity
import kz.tilsimsozder.common.BaseBottomSheetDialog
import kz.tilsimsozder.common.BaseFragment
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.prayers.adapter.PrayerAdapter
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.prayers.ui.LanguageDialogFragment
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage
import kz.tilsimsozder.tilsim.ui.TilsimDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MessageFragment: BaseFragment<MessageContract.View, MessageContract.Presenter>(),
    MessageContract.View {

    companion object {
        fun create() = MessageFragment()
    }

    override val presenter: MessagePresenter by viewModel { parametersOf(requireContext()) }
    private var prayerAdapter: PrayerAdapter? = null
    private val analytics = Analytics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_message, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            SharedPreference(it).setCurrentFragmentName(FragmentName.PRAYER.ordinal)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        presenter.loadPrayers()
        presenter.checkLanguage()
        analytics.openPrayerPage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SupportLanguage.KZ.code,
                SupportLanguage.UZ.code,
                SupportLanguage.RU.code -> restart()
            }
        }
    }

    override fun showLanguageDialog() {
        val bottomSheetDialogFragment =
            BaseBottomSheetDialog.create(LanguageDialogFragment.create())
        bottomSheetDialogFragment.show(childFragmentManager, LanguageDialogFragment.LANGUAGE_DIALOG_FRAGMENT)
    }

    private fun setupView() {
        context?.let { analytics.setup(it) }


        prayerAdapter = PrayerAdapter(
            textClickListener = { title, body, url ->
                presenter.selectedPrayer(title, body, url)
                analytics.showPrayer(title)
            },
            favouriteClickListener = { id ->
                presenter.setFavourite(id)
            })

        val selectPrayerManager = LinearLayoutManager(context)
        prayerListRecyclerView.apply {
            layoutManager = selectPrayerManager
            adapter = prayerAdapter
        }

        /*new*/
        searchPrayer.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.sortPrayer(newText)
                return false
            }
        })

        searchPrayer.setOnClickListener {
            searchPrayer.onActionViewExpanded()
        }
    }

    override fun showPrayers(prayers: List<Prayer>) {
        prayerAdapter?.addItems(prayers)
    }

    override fun sharePrayer(urlApp: String, title: String, body: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$urlApp \n\n $title \n $body")
        shareIntent.type = "text/plain"
        context?.startActivity(shareIntent)
    }

    override fun showPrayerDialog(title: String, body: String, url: String) {
        val bottomSheetDialogFragment =
            BaseBottomSheetDialog.create(TilsimDialogFragment.create(
                title,
                body,
                url
            ))
        bottomSheetDialogFragment.show(childFragmentManager, TilsimDialogFragment.TILSIM_DIALOG_FRAGMENT)
    }

    override fun scrollToTop() {
        prayerListRecyclerView.smoothScrollToPosition(0)
    }

    private fun restart() {
        activity?.finish()
        activity?.overridePendingTransition(0, 0)
        val intent = Intent(activity, MainActivity::class.java)
        activity?.overridePendingTransition(0, 0)
        activity?.startActivity(intent)
    }
}