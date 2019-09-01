package kz.tilsimsozder.tilsim.contract

import kz.tilsimsozder.common.MvpPresenter
import kz.tilsimsozder.common.MvpView
import kz.tilsimsozder.tilsim.model.Tilsim

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