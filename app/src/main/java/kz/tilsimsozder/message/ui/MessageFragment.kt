package kz.tilsimsozder.message.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.ui.BotAdapter
import kz.tilsimsozder.common.BaseFragment
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageFragment : BaseFragment<MessageContract.View, MessageContract.Presenter>(),
    MessageContract.View {

    companion object {
        fun create() = MessageFragment()
    }

    private var newsAdapter: BotAdapter? = null
    private val presenterImpl: MessagePresenter by viewModel()
    override val presenter: MessageContract.Presenter
        get() = presenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { SharedPreference(it).setCurrentFragmentName(FragmentName.PRAYER.ordinal) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_message, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun show() {
        TODO("Not yet implemented")
    }
}