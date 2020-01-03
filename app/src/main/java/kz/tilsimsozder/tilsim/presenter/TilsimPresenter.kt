package kz.tilsimsozder.tilsim.presenter

import android.annotation.SuppressLint
import kz.tilsimsozder.common.BasePresenter
import kz.tilsimsozder.prayers.api.PrayersApi
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.contract.TilsimContract
import kz.tilsimsozder.tilsim.model.Tilsim

class TilsimPresenter(
    private val prayersApi: PrayersApi
) : BasePresenter<TilsimContract.View>(),
    TilsimContract.Presenter {

    private var notesTitle = listOf<String>()
    private var notesBody = listOf<String>()
    private var tilsimList = mutableListOf<Tilsim>()

    @SuppressLint("DefaultLocale")
    override fun loadTilsim() {
        tilsimList.clear()
        tilsimList.addAll(prayersApi.getTilsimList())

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
        view?.showBubbleSeekBar(tilsimList.size)
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