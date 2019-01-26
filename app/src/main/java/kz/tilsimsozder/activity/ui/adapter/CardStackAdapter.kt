package kz.tilsimsozder.activity.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kz.tilsimsozder.R

class CardStackAdapter(private var title: MutableList<String>, private var content: MutableList<String>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(inflater.inflate(R.layout.item_card_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = title[position]
        holder.content.text = content[position]
        holder.itemView.setOnClickListener { v ->
            Toast.makeText(v.context, title[position], Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = title.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTextView)
        val content: TextView = view.findViewById(R.id.contentTextView)
    }
}