package kz.tilsimsozder.bots.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.fragment_bots.*
import kz.azatserzhanov.test.common.BaseFragment
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.contract.BotContract
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.bots.presenter.BotPresenter
import org.koin.androidx.viewmodel.ext.android.viewModel

class BotFragment : BaseFragment<BotContract.View, BotContract.Presenter>(),
        BotContract.View {

    companion object {
        fun create() = BotFragment()
    }

    private val presenterImpl: BotPresenter by viewModel()
    override val presenter: BotContract.Presenter
        get() = presenterImpl
    private var botAdapter: BotAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_bots, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        botAdapter = BotAdapter(
                clickListener = {
                    openBot(botAdapter?.getItem(it))
                }
        )

        var botManager = FlexboxLayoutManager(context)
        botManager.flexDirection = FlexDirection.ROW
        botManager.justifyContent = JustifyContent.CENTER

        botRecyclerView.apply {
            layoutManager = botManager
            adapter = botAdapter
        }

        presenter.loadBots()
    }

    override fun showBots(bots: List<Bot>) {
        botAdapter?.addItems(bots)
    }

    private fun openBot(bot: Bot?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bot?.url))
        startActivity(intent)
    }
}