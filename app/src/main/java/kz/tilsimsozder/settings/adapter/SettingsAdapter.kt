package kz.tilsimsozder.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_settings.view.contentTextView
import kotlinx.android.synthetic.main.item_settings.view.settingsContainer
import kotlinx.android.synthetic.main.item_settings.view.switchView
import kotlinx.android.synthetic.main.item_settings.view.thumbImageView
import kz.tilsimsozder.R
import kz.tilsimsozder.prayers.model.SettingsItem

class SettingsAdapter(
    private val menuItemListener: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val settings = mutableListOf<SettingsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SettingsViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = settings.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SettingsViewHolder).bind(settings[position], menuItemListener)
    }

    fun addItems(list: List<SettingsItem>) {
        settings.clear()
        settings.addAll(list)
        notifyDataSetChanged()
    }

    private class SettingsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_settings, parent, false)) {
        private val settingsContainer = itemView.settingsContainer
        private val thumbImageView = itemView.thumbImageView
        private val contentTextView = itemView.contentTextView
        private val switchView = itemView.switchView

        fun bind(settings: SettingsItem, textClickListener: (position: Int) -> Unit) {
            settingsContainer.setOnClickListener { textClickListener(adapterPosition) }
            contentTextView.text = settings.title
            thumbImageView.setImageDrawable(itemView.context.getDrawable(settings.icon))
            switchView.isVisible = settings.isSwitchVisible
        }
    }
}