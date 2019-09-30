package kz.tilsimsozder.tilsim.presenter

import android.content.Context
import kz.tilsimsozder.R
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.contract.TilsimContract
import kz.tilsimsozder.tilsim.model.Tilsim

class TilsimPresenter(private val context: Context) : BasePresenter<TilsimContract.View>(),
        TilsimContract.Presenter {

    private var tilsimTitle = listOf<String>()
    private var tilsimBody = listOf<String>()
    private var notesTitle = listOf<String>()
    private var notesBody = listOf<String>()
    private var tilsimList = mutableListOf<Tilsim>()
    private var counter = 0

    override fun loadTilsim() {
        tilsimTitle = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_title).toList()
        tilsimBody = context.applicationContext.resources.getStringArray(R.array.tilsim_sozder_content).toList()
        notesTitle = context.applicationContext.resources.getStringArray(R.array.notes_title).toList()
        notesBody = context.applicationContext.resources.getStringArray(R.array.notes_value).toList()

        tilsimList.clear()
        counter = 0
        tilsimList.addAll(
                tilsimTitle.map { Tilsim(it, "", position = counter++) }
                        .toList()
                        .apply {
                            this.forEachIndexed { index, tilsimsoz ->
                                tilsimsoz.body = tilsimBody[index]
                            }
                        })

        tilsimList.map {
            val bodyText: MutableList<String> = it.body.split(" ").toMutableList()
            val titleText: MutableList<String> = it.title.split(" ").toMutableList()
            val pattern = "[*]".toRegex()
            var resultText = ""

            bodyText.forEach { text ->
                if (pattern.containsMatchIn(text)) {
                    var count = 0

                    notesTitle.forEach { title ->
                        if (text.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                            resultText = "\n $title - " + notesBody[count]
                        }
                        count++
                    }
                }
            }

            titleText.forEach { text ->
                if (pattern.containsMatchIn(text)) {
                    var count = 0

                    notesTitle.forEach { title ->
                        if (text.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                            resultText += "\n $title - " + notesBody[count]
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

    override fun shareTilsim(position: Int) {
        view?.showShare(tilsimList[position].title, tilsimList[position].body)
    }

    override fun setBottomSheetDialog(position: Int) {
        view?.showDialog(tilsimList[position])
    }

    override fun openServiceTilsimPosition() {
        view?.showTilsim(TilsimService.RANDOM_TILSIM)
    }
}