package kz.tilsimsozder.prayers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_prayer.view.*
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsimsozder.model.Prayer

/**
 * Created by azatserzhanov on 13.12.15.
 */
class PrayerAdapter(
        private val clickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val prayers = mutableListOf<Prayer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SelectPrayerViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = prayers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectPrayerViewHolder).bind(prayers[position], clickListener)
    }

    fun addItems(list: List<Prayer>) {
        prayers.addAll(list)
        notifyItemRangeInserted(prayers.size + 1, prayers.size)
    }

    private class SelectPrayerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.select_prayer, parent, false)) {
        private val prayerNumberTextView = itemView.prayerNumberTextView
        private val prayerNameTextView = itemView.prayerNameTextView
        private val selectPrayerLinearLayout = itemView.selectPrayerLinearLayout

        fun bind(prayer: Prayer, clickListener: (position: Int) -> Unit) {
            prayerNameTextView.text = prayer.title
            prayerNumberTextView.text = adapterPosition.toString()
            selectPrayerLinearLayout.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
}