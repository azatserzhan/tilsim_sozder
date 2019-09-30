package kz.tilsimsozder.news.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*
import kz.tilsimsozder.common.BaseFragment
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.bots.ui.BotAdapter
import kz.tilsimsozder.common.BaseBottomSheetDialog
import kz.tilsimsozder.news.contract.NewsContract
import kz.tilsimsozder.news.presenter.NewsPresenter
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment<NewsContract.View, NewsContract.Presenter>(),
        NewsContract.View {

    companion object {
        fun create() = NewsFragment()
    }

    private var newsAdapter: BotAdapter? = null
    private val presenterImpl: NewsPresenter by viewModel()
    override val presenter: NewsContract.Presenter
        get() = presenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { SharedPreference(it).setCurrentFragmentName(FragmentName.NEWS.ordinal) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = BotAdapter(
            clickListener = {
                openBot(newsAdapter?.getItem(it))
            },
            shareListener = {
                share(it)
            }
        )

        val newsManager = LinearLayoutManager(context)
        newsRecyclerView.apply {
            layoutManager = newsManager
            adapter = newsAdapter
        }

        presenter.loadNews()
    }

    override fun showNews(bots: List<Bot>) {
        newsAdapter?.addItems(bots)
    }

    private fun openBot(bot: Bot?) {
        bot?.let {
            val bottomSheetDialogFragment =
                BaseBottomSheetDialog.create(NewsDialogFragment.create(
                    bot.title,
                    bot.url
                ))
            bottomSheetDialogFragment.show(childFragmentManager, NewsDialogFragment.NEWS_DIALOG_FRAGMENT)
        }
    }

    private fun share(url: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.type = "text/plain"
        context?.startActivity(shareIntent)
    }
}