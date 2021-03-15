package kz.tilsimsozder.tilsim.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.bodyTextView
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.copyContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.exitImageView
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.playImageButton
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.progressBar
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.scrollContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.sendErrorContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.shareContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.titleTextView
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.zoomInButton
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.zoomOutButton
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.preference.PreferenceContract
import org.koin.android.ext.android.inject

private const val TILSIM_TITLE = "TILSIM_TITLE"
private const val TILSIM_BODY = "TILSIM_BODY"
private const val TILSIM_URL = "TILSIM_URL"
private const val CONTENT_TEXT_MAX_SIZE = 26
private const val CONTENT_TEXT_MIN_SIZE = 12

class TilsimDialogFragment : Fragment() {

    companion object {
        const val TILSIM_DIALOG_FRAGMENT = "TILSIM_DIALOG_FRAGMENT"
        fun create(title: String, body: String, url: String = "") = TilsimDialogFragment().apply {
            arguments = Bundle().apply {
                putString(TILSIM_TITLE, title)
                putString(TILSIM_BODY, body)
                putString(TILSIM_URL, url)
            }
        }
    }

    private val title: String by lazy { arguments?.getString(TILSIM_TITLE) ?: "" }
    private val body: String by lazy { arguments?.getString(TILSIM_BODY) ?: "" }
    private val url: String by lazy { arguments?.getString(TILSIM_URL) ?: "" }
    private val analytics = Analytics()
    private val sharedPreferences: PreferenceContract by inject()
    private var contentTextSize = 16
    private val player = MediaPlayer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tilsim_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView.text = title
        bodyTextView.text = body
        contentTextSize = sharedPreferences.getDialogTextSize()
        bodyTextView.textSize = contentTextSize.toFloat()

        shareContainer.setOnClickListener {
            val shareIntent = Intent()
            val shareText = "$title \n $body \n https://play.google.com/store/apps/details?id=kz.tilsimsozder"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            shareIntent.type = "text/plain"
            context?.startActivity(shareIntent)
            analytics.shareTilsim(title)
        }

        sendErrorContainer.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "azatserzhan@gmail.com", null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, requireContext().getString(R.string.dialog_abuse))
            emailIntent.putExtra(Intent.EXTRA_TEXT, "$title \n $body")
            startActivity(Intent.createChooser(emailIntent, "Хат жазу..."))
        }

        copyContainer.setOnClickListener {
            val myClipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val myClip = ClipData.newPlainText("text", title + body)
            myClipboard.setPrimaryClip(myClip)

            val text = requireContext().getString(R.string.dialog_copied)
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
        }

        exitImageView.setOnClickListener {
            (parentFragment as? BottomSheetDialogFragment)?.dialog?.cancel()
        }

        zoomOutButton.setOnClickListener {
            if (contentTextSize > CONTENT_TEXT_MIN_SIZE) {
                contentTextSize--
                bodyTextView.textSize = contentTextSize.toFloat()
                sharedPreferences.setDialogTextSize(contentTextSize)
            }
        }

        zoomInButton.setOnClickListener {
            if (contentTextSize < CONTENT_TEXT_MAX_SIZE) {
                contentTextSize++
                bodyTextView.textSize = contentTextSize.toFloat()
                sharedPreferences.setDialogTextSize(contentTextSize)

                scrollContainer.post {
                    scrollContainer.fullScroll(View.FOCUS_DOWN)
                }
            }
        }

        scrollContainer.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            var measure = v?.getChildAt(0)?.measuredHeight ?: 0
            measure -= v?.height ?: 0
            progressBar.max = measure
            progressBar.progress = scrollY
        }

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStart() {
        super.onStart()
        setupAudio()
    }

    private fun setupAudio() {
        if (url.isNotEmpty() && haveNetworkConnection()) {
            playImageButton.isVisible = url.isNotEmpty()
            player.setDataSource(requireContext(), Uri.parse(url))
            player.prepare()
        }

        playImageButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                playImageButton.setBackgroundResource(R.drawable.ic_play_button)
            } else {
                playImageButton.setBackgroundResource(R.drawable.ic_pause_button)
                player.start()
            }
        }
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false

        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
}
