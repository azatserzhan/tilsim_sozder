package kz.tilsimsozder.tilsim.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.bodyTextView
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.copyContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.sendErrorContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.shareContainer
import kotlinx.android.synthetic.main.fragment_tilsim_dialog.titleTextView
import kz.tilsimsozder.R

private const val TILSIM_TITLE = "TILSIM_TITLE"
private const val TILSIM_BODY = "TILSIM_BODY"

class TilsimDialogFragment : Fragment() {

    companion object {
        const val TILSIM_DIALOG_FRAGMENT = "TILSIM_DIALOG_FRAGMENT"
        fun create(title: String, body: String) = TilsimDialogFragment().apply {
            arguments = Bundle().apply {
                putString(TILSIM_TITLE, title)
                putString(TILSIM_BODY, body)
            }
        }
    }

    private val title: String by lazy { arguments?.getString(TILSIM_TITLE) ?: "" }
    private val body: String by lazy { arguments?.getString(TILSIM_BODY) ?: "" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tilsim_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView.text = title
        bodyTextView.text = body

        shareContainer.setOnClickListener {
            val shareIntent = Intent()
            val shareText = "$title + \n $body \n https://play.google.com/store/apps/details?id=kz.tilsimsozder"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            shareIntent.type = "text/plain"
            context?.startActivity(shareIntent)
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
            myClipboard.primaryClip = myClip

            val text = requireContext().getString(R.string.dialog_copied)
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(requireContext(), text, duration)
            toast.show()
        }
    }
}
