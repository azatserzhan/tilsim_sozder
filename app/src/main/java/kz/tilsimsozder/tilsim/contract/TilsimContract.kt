package kz.tilsimsozder.tilsim.contract

import kz.azatserzhanov.test.common.MvpPresenter
import kz.azatserzhanov.test.common.MvpView
import kz.tilsimsozder.tilsim.model.Tilsim
import kz.tilsimsozder.tilsimsozder.model.Prayer

interface TilsimContract {

    interface View : MvpView {
        fun showTilsim(tilsimList: List<Tilsim>)
        fun showTilsim(position: Int)
        fun showBubbleSeekBar(max: Int)
        fun showShare(title: String, body: String)
        fun showDialog(tilsim: Tilsim)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadTilsim()
        fun setupBubbleSeekBar()
        fun shareTilsim(position: Int)
        fun setBottomSheetDialog(position: Int)
        fun openServiceTilsimPosition()
    }
}