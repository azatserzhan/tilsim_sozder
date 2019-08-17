package kz.tilsimsozder.tilsim.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_tilsim.view.*
import kz.tilsimsozder.R
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.model.Tilsim

class TilsimAdapter(
        val counterListener: () -> Unit,
        val bodyListner: (position: Int) -> Unit,
        val shareListner: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tilsimList = mutableListOf<Tilsim>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TilsimViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TilsimViewHolder).bind(
                tilsimList[position],
                bodyListner,
                counterListener,
                shareListner,
                tilsimList.size
        )
    }

    override fun getItemCount(): Int = tilsimList.size

    fun addItems(list: List<Tilsim>) {
        tilsimList.addAll(list)
        notifyDataSetChanged()
    }

    fun setRandomPosition() {
        val listLength = tilsimList.size - 2
        TilsimService.RANDOM_TILSIM = (0..listLength).shuffled().last()
        notifyDataSetChanged()
    }

    fun setPosition(position: Int) {
        if (position < tilsimList.size - 1) {
            TilsimService.RANDOM_TILSIM = position
            notifyDataSetChanged()
        } else {
            Log.d("error", "Position is bigges: $position")
        }
    }

    private class TilsimViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_tilsim, parent, false)) {

        private val titleTextView = itemView.titleTextView
        private val contentTextView = itemView.contentTextView
        private val cardCounterTextView = itemView.cardCounterTextView
        private val shareImageView = itemView.shareImageView

        fun bind(
                tilsim: Tilsim,
                bodyListner: (position: Int) -> Unit,
                counterListener: () -> Unit,
                shareListner: (position: Int) -> Unit,
                tilsimSize: Int
        ) {
            titleTextView.text = tilsim.title
            contentTextView.text = tilsim.body + "\n" + tilsim.note
            cardCounterTextView.text = "${tilsim.position} / $tilsimSize"
            cardCounterTextView.setOnClickListener { counterListener() }
            shareImageView.setOnClickListener { shareListner(adapterPosition) }
            contentTextView.setOnClickListener { bodyListner(adapterPosition) }

            // cardCounterTextView.text = newPosiotion.toString() + " / " + tilsimList.size
        }
    }
}