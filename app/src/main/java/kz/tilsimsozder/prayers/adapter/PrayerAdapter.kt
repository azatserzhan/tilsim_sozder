package kz.tilsimsozder.prayers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_prayer.view.*
import kz.tilsimsozder.R
import kz.tilsimsozder.prayers.model.Prayer


class PrayerAdapter(
        private val textClickListener: (position: Int) -> Unit,
        private val favouriteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val prayers = mutableListOf<Prayer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SelectPrayerViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = prayers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectPrayerViewHolder).bind(
                prayers[position],
                textClickListener,
                favouriteClickListener
        )
    }

    fun addItems(list: List<Prayer>) {
        prayers.addAll(list)
        notifyDataSetChanged()
    }

    private class SelectPrayerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.select_prayer, parent, false)) {
        private val prayerNameTextView = itemView.prayerNameTextView
        private val selectPrayerLinearLayout = itemView.selectPrayerLinearLayout
        private val favouriteImageView = itemView.favouriteImageView

        fun bind(
                prayer: Prayer,
                textClickListener: (position: Int) -> Unit,
                favouriteClickListener: (position: Int) -> Unit
        ) {
            prayerNameTextView.text = prayer.title
            selectPrayerLinearLayout.setOnClickListener {
                textClickListener(adapterPosition)
            }
            favouriteImageView.setOnClickListener {
                favouriteClickListener(adapterPosition)
            }
        }
    }
}