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
    private var notesTitle = listOf<String>()
    private var notesBody = listOf<String>()
    private var tilsimList = mutableListOf<Tilsim>()
    private var positionTilsim = 0

    override fun loadTilsim() {
        tilsimTitle = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_title).toList()
        tilsimBody = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_content).toList()
        notesTitle = context.applicationContext.resources.getStringArray(R.array.notes_title).toList()
        notesBody = context.applicationContext.resources.getStringArray(R.array.notes_value).toList()

        tilsimList.addAll(
                tilsimTitle.map { Tilsim(it, "") }
                        .toList()
                        .apply {
                            this.forEachIndexed { index, tilsimsoz ->
                                tilsimsoz.body = tilsimBody[index]
                            }
                        })

        tilsimList.map {
            val data: MutableList<String> = it.body.split(" ").toMutableList()
            val pattern = "[*]".toRegex()
            var resultText = ""

            data.forEach { text ->
                if (pattern.containsMatchIn(text)) {
                    var count = 0

                    notesTitle.forEach { title ->
                        if (text.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                            resultText = "$title - " + notesBody[count]
                        }
                        count++
                    }
                }
            }

            it.note = resultText
            it
        }

        view?.showTilsim(tilsimList)
    }

    override fun setupBubbleSeekBar() {
        view?.showBubbleSeekBar(tilsimBody.size)
    }
}