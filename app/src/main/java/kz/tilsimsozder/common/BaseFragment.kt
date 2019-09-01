package kz.tilsimsozder.common

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.tilsimsozder.R

abstract class BaseFragment<V : MvpView, P : MvpPresenter<V>> : BaseMvpFragment<V, P>() {

    fun replaceFragment(
            fragment: Fragment,
            @IdRes layoutId: Int = R.id.container,
            addToBackStack: Boolean = true,
            tag: String = fragment::class.java.simpleName
    ) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(layoutId, fragment)
            ?.apply { if (addToBackStack) addToBackStack(tag) }
            ?.commitAllowingStateLoss()
    }

    fun addFragment(
        fragment: Fragment,
        @IdRes layoutId: Int = R.id.container,
        addToBackStack: Boolean = true,
        tag: String = fragment::class.java.simpleName
    ) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.add(layoutId, fragment)
            ?.apply { if (addToBackStack) addToBackStack(tag) }
            ?.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentManager?.fragments?.forEach {
            if (it is BottomSheetDialogFragment) {
                it.dismissAllowingStateLoss()
            }
        }
    }
}