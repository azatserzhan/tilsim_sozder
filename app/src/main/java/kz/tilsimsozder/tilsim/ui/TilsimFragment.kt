package kz.tilsimsozder.tilsim.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import com.xw.repo.BubbleSeekBar
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_tilsim.card_stack_view
import kotlinx.android.synthetic.main.item_change_tilsim.*
import kz.azatserzhanov.test.common.BaseFragment
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.contract.TilsimContract
import kz.tilsimsozder.tilsim.model.Tilsim
import kz.tilsimsozder.tilsim.presenter.TilsimPresenter
import kz.tilsimsozder.tilsimsozder.SharedPreference
import kz.tilsimsozder.tilsim.adapter.TilsimAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CARD_VISIBLE_ITEM_COUNT = 3
private const val CARD_TRANSLATION_INTERVAL = 4.0f
private const val CARD_SCALE_INTERVAL = 0.95f
private const val CARD_SWIPE_THRESHOLD = 0.3f
private const val CARD_MAX_DEGREE = -30.0f

class TilsimFragment : BaseFragment<TilsimContract.View, TilsimContract.Presenter>(),
        TilsimContract.View, CardStackListener {

    companion object {
        fun create() = TilsimFragment()
    }

    private val presenterImpl: TilsimPresenter by viewModel()
    override val presenter: TilsimContract.Presenter
        get() = presenterImpl

    private var cardAdapter: TilsimAdapter? = null

    private var notesTitle: MutableList<String> = mutableListOf()
    private var notesBody: MutableList<String> = mutableListOf()

    private val analytics = Analytics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tilsim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupView()
        // analytics.openTilsimPage()
        presenter.loadTilsim()
        presenter.setupBubbleSeekBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let { SharedPreference(it).setIsTilsimPage(false) }
    }

    override fun onDestroy() {
        context?.let { SharedPreference(it).setIsTilsimPage(false) }
        super.onDestroy()
    }

    /*Tilsim*/
    override fun showTilsim(tilsimList: List<Tilsim>) {
        cardAdapter?.addItems(tilsimList)
        Log.d("azat", "showTilsim")
    }

    override fun showBubbleSeekBar(max: Int) {
        changeTilsimBubbleSeekBar.configBuilder
                .max(max.toFloat())
                .progress(TilsimService.RANDOM_TILSIM.toFloat())
                .build()

        changeTilsimBubbleSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun getProgressOnActionUp(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float
            ) {
                changeTilsimLinearLayout.visibility = View.GONE
            }

            override fun getProgressOnFinally(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
            ) {
            }

            override fun onProgressChanged(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
            ) {
                cardAdapter?.setPosition(progress)
            }
        }

        changeTilsimLinearLayout.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    /*Care Stack*/
    override fun onCardDisappeared(view: View?, position: Int) {}

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        // analytics.showTilsim()
    }

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardRewound() {}

    private fun setupView() {
        setupCardStackView()

        randomButton.setOnClickListener {
            cardAdapter?.setRandomPosition()
            analytics.randomButtonClicked()
            changeTilsimBubbleSeekBar.setProgress(TilsimService.RANDOM_TILSIM.toFloat())
        }
    }

    private fun setupCardStackView() {
        val cardManager = CardStackLayoutManager(requireActivity())
        cardManager.setStackFrom(StackFrom.Top)
        cardManager.setVisibleCount(CARD_VISIBLE_ITEM_COUNT)
        cardManager.setTranslationInterval(CARD_TRANSLATION_INTERVAL)
        cardManager.setScaleInterval(CARD_SCALE_INTERVAL)
        cardManager.setSwipeThreshold(CARD_SWIPE_THRESHOLD)
        cardManager.setMaxDegree(CARD_MAX_DEGREE)
        cardManager.setDirections(Direction.FREEDOM)
        cardManager.setCanScrollHorizontal(true)
        cardManager.setCanScrollVertical(true)

        cardAdapter = TilsimAdapter(changeTilsimListener = {
            changeTilsimLinearLayout.isVisible = true
        })
        card_stack_view.adapter = cardAdapter
        card_stack_view.layoutManager = cardManager
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = true
            }
        }
    }

    /*private fun pushNotificationNumber() {
        if (TilsimService.RANDOM_TILSIM != 0) {
            TextViewHeader.text = tilsimsTitle[TilsimService.RANDOM_TILSIM]
            TextViewContent.text = tilsimsBidy[TilsimService.RANDOM_TILSIM]
        }
    }*/

}
