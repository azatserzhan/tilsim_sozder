package kz.tilsimsozder.tilsimsozder.ui.mainactivity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.tilsimsozder.R

class TilsimsozderFragment : Fragment() {

    companion object {
        fun newInstance() = TilsimsozderFragment()
    }

    private lateinit var viewModel: TilsimsozderFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tilsim_sozder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TilsimsozderFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
