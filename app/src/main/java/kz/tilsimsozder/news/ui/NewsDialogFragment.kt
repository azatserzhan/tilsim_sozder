package kz.tilsimsozder.news.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_news_dialog.exitImageView
import kotlinx.android.synthetic.main.fragment_news_dialog.newsWebView
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.titleTextView
import kz.tilsimsozder.R

private const val URL_TITLE = "URL_TITLE"
private const val URL_BODY = "URL_BODY"

class NewsDialogFragment : Fragment() {

    companion object {
        const val NEWS_DIALOG_FRAGMENT = "NEWS_DIALOG_FRAGMENT"
        fun create(title: String, body: String) = NewsDialogFragment().apply {
            arguments = Bundle().apply {
                putString(URL_TITLE, title)
                putString(URL_BODY, body)
            }
        }
    }

    private val title: String by lazy { arguments?.getString(URL_TITLE) ?: "" }
    private val url: String by lazy { arguments?.getString(URL_BODY) ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_news_dialog, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView.text = title

        newsWebView.loadUrl(url)
        newsWebView.settings.javaScriptEnabled = true
        newsWebView.webViewClient = WebViewClient()

        exitImageView.setOnClickListener {
            (parentFragment as? BottomSheetDialogFragment)?.dialog?.cancel()
        }
    }
}
