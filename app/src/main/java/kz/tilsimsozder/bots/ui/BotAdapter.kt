package kz.tilsimsozder.bots.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bot.view.botDescriptionTextView
import kotlinx.android.synthetic.main.item_bot.view.botImageView
import kotlinx.android.synthetic.main.item_bot.view.botTitleTextView
import kotlinx.android.synthetic.main.item_bot.view.serviceContainer
import kotlinx.android.synthetic.main.item_bot.view.shareImageView
import kz.tilsimsozder.R
import kz.tilsimsozder.bots.model.Bot

class BotAdapter(
    private val clickListener: (position: Int) -> Unit,
    private val shareListener: (url: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val bots = mutableListOf<Bot>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BotViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = bots.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BotViewHolder).bind(bots[position], clickListener, shareListener)
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
        private val botDescriptionTextView = itemView.botDescriptionTextView
        private val botImageView = itemView.botImageView
        private val serviceContainer = itemView.serviceContainer
        private val shareImageView = itemView.shareImageView

        fun bind(
            bot: Bot,
            clickListener: (position: Int) -> Unit,
            shareListener: (url: String) -> Unit
        ) {
            botTitleTextView.text = if (bot.title != 0) botTitleTextView.context.getString(bot.title) else ""
            botDescriptionTextView.text = if (bot.description != 0) botDescriptionTextView.context.getString(bot.description) else ""
            botImageView.setBackgroundResource(bot.imageRes)
            serviceContainer.setOnClickListener { clickListener(adapterPosition) }
            shareImageView.setOnClickListener { shareListener("${bot.title}\n ${bot.url}") }
        }
    }
}