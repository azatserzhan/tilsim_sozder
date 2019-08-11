package kz.tilsimsozder.tilsimsozder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bot.view.*
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsimsozder.model.Bot


class BotAdapter(
        private val clickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val bots = mutableListOf<Bot>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BotViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = bots.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BotViewHolder).bind(bots[position], clickListener)
    }

    fun addItems(list: List<Bot>) {
        bots.addAll(list)
        // notifyItemRangeInserted(bots.size + 1, bots.size)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Bot = bots[position]

    private class BotViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_bot, parent, false)) {
        private val botTitleTextView = itemView.botTitleTextView
        private val botImageView = itemView.botImageView

        fun bind(bot: Bot, clickListener: (position: Int) -> Unit) {
            botTitleTextView.text = bot.title
            botImageView.setBackgroundResource(bot.imageRes)
            botImageView.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
}