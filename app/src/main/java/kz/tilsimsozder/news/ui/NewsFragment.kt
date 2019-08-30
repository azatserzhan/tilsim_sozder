package kz.tilsimsozder.news.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_news.*
import kz.azatserzhanov.test.common.BaseFragment
import kz.tilsimsozder.R
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

        presenter.loadNews()
    }

    override fun showNews(url: String) {
        newsWebView.loadUrl(url)
        newsWebView.settings.javaScriptEnabled = true
        newsWebView.webViewClient = WebViewClient()
    }
}