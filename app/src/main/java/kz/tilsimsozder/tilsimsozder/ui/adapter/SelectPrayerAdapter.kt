package kz.tilsimsozder.tilsimsozder.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.select_prayer.view.prayerNameTextView
import kotlinx.android.synthetic.main.select_prayer.view.prayerNumberTextView
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsimsozder.model.Prayer

/**
 * Created by azatserzhanov on 13.12.15.
 */
class SelectPrayerAdapter(
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

    private class SelectPrayerViewHolder(val inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.select_prayer, parent, false)) {
        private val prayerNumberTextView = itemView.prayerNumberTextView
        private val prayerNameTextView = itemView.prayerNameTextView

        fun bind(prayer: Prayer, clickListener: (position: Int) -> Unit) {
            prayerNameTextView.text = prayer.title
            prayerNameTextView.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
    /*private var typeface: Typeface? = Typeface.createFromAsset(context.getAssets(), "font/kz_r.ttf");

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var newView = view
        if (newView == null) {
            val vi = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            newView = vi.inflate(id, null)
        }

        val text = newView!!.findViewById<View>(R.id.textView) as TextView

        text.text = Html.fromHtml(items[position])
        text.typeface = typeface
        text.setTextColor(Color.parseColor("#ffffff"))

        val textViewFirstNumber = newView.findViewById<View>(R.id.textViewFirstNumber) as TextView
        textViewFirstNumber.text = position.toString()


        return newView
    }*/
}