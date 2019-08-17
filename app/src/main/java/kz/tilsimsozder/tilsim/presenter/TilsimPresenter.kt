package kz.tilsimsozder.tilsim.presenter

import android.content.Context
import kz.azatserzhanov.test.common.BasePresenter
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.tilsim.contract.TilsimContract
import kz.tilsimsozder.tilsimsozder.model.Prayer


class TilsimPresenter(private val context: Context, private val analytics: Analytics) : BasePresenter<TilsimContract.View>(),
        TilsimContract.Presenter {

    private var tilsimTitle = listOf<String>()
    private var tilsimBody = listOf<String>()
    private var positionTilsim = 0

    override fun loadPrayers() {
        tilsimTitle = context.applicationContext.resources.getStringArray(R.array.prayer_name).toList()
        tilsimBody = context.applicationContext.resources.getStringArray(R.array.prayer_value).toList()

        val prayers = tilsimTitle
                .map { Prayer(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.content = tilsimBody[index]
                    }
                }
        view?.showPrayers(prayers)
    }
}