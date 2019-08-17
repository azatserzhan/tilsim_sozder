package kz.tilsimsozder.tilsim.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.tilsimsozder.R
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.model.Tilsim

class TilsimAdapter(
        val changeTilsimListener: (position: Int) -> Unit
) : RecyclerView.Adapter<TilsimAdapter.TilsimViewHolder>() {

    private lateinit var context: Context
    private var tilsimData = mutableListOf<Tilsim>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TilsimViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return TilsimViewHolder(inflater.inflate(R.layout.item_card_spot, parent, false))
    }

    override fun onBindViewHolder(holder: TilsimViewHolder, position: Int) {
        var newPosiotion = TilsimService.RANDOM_TILSIM + position
        if (newPosiotion >= tilsimData.size) {
            newPosiotion = TilsimService.RANDOM_TILSIM - position
        }
        holder.titleTextView.text = tilsimData[newPosiotion].title
        holder.contentTextView.text = tilsimData[newPosiotion].body
        //setNotes(holder.contentTextView)
        holder.cardCounterTextView.text = newPosiotion.toString() + " / " + tilsimData.size

        holder.shareImageView.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, tilsimData[newPosiotion].title + "\n" + tilsimData[newPosiotion].body)
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }
        holder.cardCounterTextView.setOnClickListener {
            changeTilsimListener(position)
        }
    }

    override fun getItemCount(): Int = tilsimData.size

    fun addItems(list: List<Tilsim>) {
        tilsimData.addAll(list)
        notifyDataSetChanged()
    }

    fun setRandomPosition() {
        val listLength = tilsimData.size - 2
        TilsimService.RANDOM_TILSIM = (0..listLength).shuffled().last()
        notifyDataSetChanged()
    }

    fun setPosition(position: Int) {
        if (position < tilsimData.size - 1) {
            TilsimService.RANDOM_TILSIM = position
            notifyDataSetChanged()
        } else {
            Log.d("error", "Position is bigges: $position")
        }
    }

    // TODO: сделай setNotes
    /*private fun setNotes(TextViewContent: TextView) {
        val data: MutableList<String> = TextViewContent.text.split(" ").toMutableList()
        val pattern = "[*]".toRegex()

        TextViewContent.append("\n\n\n")
        data.forEach { text ->
            if (pattern.containsMatchIn(text)) {
                var count = 0
                TilsimsozderFragment.LIST_TITLE_NOTES.forEach { title ->
                    if (text.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                        TextViewContent.append("$title - " + TilsimsozderFragment.LIST_CONTENT_NOTES[count])
                    }
                    count++
                }
            }
        }
    }*/

    class TilsimViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val cardCounterTextView: TextView = view.findViewById(R.id.cardCounterTextView)
        val shareImageView: ImageView = view.findViewById(R.id.shareImageView)
    }
}