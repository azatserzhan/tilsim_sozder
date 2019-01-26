package kz.tilsimsozder.activity.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kz.tilsimsozder.R
import kz.tilsimsozder.service.MyService

class CardStackAdapter(private var title: MutableList<String>, private var content: MutableList<String>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(inflater.inflate(R.layout.item_card_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var newPosiotion = MyService.RANDOM_TILSIM + position
        if(newPosiotion > title.size){
            newPosiotion = MyService.RANDOM_TILSIM - position
        }
        holder.titleTextView.text = title[newPosiotion]
        holder.contentTextView.text = content[newPosiotion]
        holder.cardCounterTextView.text = newPosiotion.toString() + " / " + title.size
        holder.shareImageView.setOnClickListener { v ->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, title[newPosiotion] + "\n" + content[newPosiotion])
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
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