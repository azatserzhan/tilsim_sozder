package kz.tilsimsozder.common

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.tilsimsozder.R

class BaseBottomSheetDialog(private val fragment: Fragment) : BottomSheetDialogFragment() {
    companion object {
        fun create(fragment: Fragment): BottomSheetDialogFragment =
                BaseBottomSheetDialog(fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BaseBottomSheetDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_bottomsheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFragment(fragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            createDialog(super.onCreateDialog(savedInstanceState) as BottomSheetDialog)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        parentFragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun createDialog(dialog: BottomSheetDialog): Dialog {

        dialog.setOnShowListener { dialogInterface ->
            val dialogView = dialogInterface as BottomSheetDialog

            val bottomSheet =
                    dialogView.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager
                .beginTransaction()
                .replace(R.id.bottomSheetContainer, fragment)
                .commitAllowingStateLoss()
    }
}
