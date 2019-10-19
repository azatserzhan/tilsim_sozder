package kz.tilsimsozder.prayers.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_language_dialog.kazakhLanguage
import kotlinx.android.synthetic.main.fragment_language_dialog.uzbekLanguage
import kz.tilsimsozder.R
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage

class LanguageDialogFragment : Fragment() {

    companion object {
        const val LANGUAGE_DIALOG_FRAGMENT = "LANGUAGE_DIALOG_FRAGMENT"
        fun create() = LanguageDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_language_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        kazakhLanguage.setOnClickListener {
            SharedPreference(requireContext()).setLanguage(SupportLanguage.KZ.code)
            sendRequest(SupportLanguage.KZ.code)
            (parentFragment as? BottomSheetDialogFragment)?.dialog?.cancel()
        }

        uzbekLanguage.setOnClickListener {
            SharedPreference(requireContext()).setLanguage(SupportLanguage.UZ.code)
            sendRequest(SupportLanguage.UZ.code)
            (parentFragment as? BottomSheetDialogFragment)?.dialog?.cancel()
        }
    }

    private fun sendRequest(request: Int) {
        parentFragment?.onActivityResult(
            request,
            Activity.RESULT_OK,
            null
        )
    }
}
