package kz.tilsimsozder.tilsimsozder.adapter

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
import kz.tilsimsozder.tilsimsozder.ui.TilsimsozderFragment

class CardStackAdapter(
        private var title: MutableList<String>,
        private var content: MutableList<String>,
        val changeTilsimListener: (position: Int) -> Unit
) :
        RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(inflater.inflate(R.layout.item_card_spot, parent, false))
    }

    fun setRandomPosition() {
        val listLength = title.size - 2
        TilsimService.RANDOM_TILSIM = (0..listLength).shuffled().last()
        notifyDataSetChanged()
    }

    fun setPosition(position: Int) {
        if (position < title.size - 1) {
            TilsimService.RANDOM_TILSIM = position
            notifyDataSetChanged()
        }else{
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var newPosiotion = TilsimService.RANDOM_TILSIM + position
        if (newPosiotion >= title.size) {
            newPosiotion = TilsimService.RANDOM_TILSIM - position
        }
        holder.titleTextView.text = title[newPosiotion]
        holder.contentTextView.text = content[newPosiotion]
        //setNotes(holder.contentTextView)
        holder.cardCounterTextView.text = newPosiotion.toString() + " / " + title.size
        holder.shareImageView.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, title[newPosiotion] + "\n" + content[newPosiotion])
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }
        holder.cardCounterTextView.setOnClickListener {
            changeTilsimListener(position)
        }
    }

    override fun getItemCount(): Int = title.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val cardCounterTextView: TextView = view.findViewById(R.id.cardCounterTextView)
        val shareImageView: ImageView = view.findViewById(R.id.shareImageView)
    }
}