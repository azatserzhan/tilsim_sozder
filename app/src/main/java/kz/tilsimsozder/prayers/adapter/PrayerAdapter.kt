package kz.tilsimsozder.prayers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.select_prayer.view.descriptionTextView
import kotlinx.android.synthetic.main.select_prayer.view.favouriteImageView
import kotlinx.android.synthetic.main.select_prayer.view.musicImageView
import kotlinx.android.synthetic.main.select_prayer.view.prayerNameTextView
import kotlinx.android.synthetic.main.select_prayer.view.selectPrayerLinearLayout
import kz.tilsimsozder.R
import kz.tilsimsozder.prayers.model.Prayer

class PrayerAdapter(
    private val textClickListener: (title: String, body: String, url: String) -> Unit,
    private val favouriteClickListener: (id: String) -> Unit
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
        prayers.clear()
        prayers.addAll(list)
        notifyDataSetChanged()
    }

    private class SelectPrayerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.select_prayer, parent, false)) {
        private val prayerNameTextView = itemView.prayerNameTextView
        private val descriptionTextView = itemView.descriptionTextView
        private val selectPrayerLinearLayout = itemView.selectPrayerLinearLayout
        private val favouriteImageView = itemView.favouriteImageView
        private val musicImageView = itemView.musicImageView

        fun bind(
            prayer: Prayer,
            textClickListener: (title: String, body: String, url: String) -> Unit,
            favouriteClickListener: (id: String) -> Unit
        ) {
            prayerNameTextView.text = prayer.title
            descriptionTextView.text = prayer.body
            val url = prayer.url ?: ""

            if (prayer.isFavourite) {
                favouriteImageView.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_favorite_active))
            } else {
                favouriteImageView.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_favorite))
            }
            selectPrayerLinearLayout.setOnClickListener {
                textClickListener(prayer.title, prayer.body, url)
            }
            favouriteImageView.setOnClickListener {
                favouriteClickListener(prayer.id)
            }

            musicImageView.isVisible = prayer.url != null
        }
    }
}