package kz.tilsimsozder.tilsim.presenter

import android.content.Context
import kz.azatserzhanov.test.common.BasePresenter
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsim.contract.TilsimContract
import kz.tilsimsozder.tilsim.model.Tilsim


class TilsimPresenter(private val context: Context) : BasePresenter<TilsimContract.View>(),
        TilsimContract.Presenter {

    private var tilsimTitle = listOf<String>()
    private var tilsimBody = listOf<String>()
    private var positionTilsim = 0

    override fun loadTilsim() {
        tilsimTitle = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_title).toList()
        tilsimBody = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_content).toList()

        val tilsimList = tilsimTitle
                .map { Tilsim(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.body = tilsimBody[index]
                    }
                }
        view?.showTilsim(tilsimList)
    }

    override fun setupBubbleSeekBar() {
        view?.showBubbleSeekBar(tilsimBody.size)
    }
}