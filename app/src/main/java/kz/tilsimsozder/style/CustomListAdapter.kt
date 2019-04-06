package kz.tilsimsozder.style

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import kz.tilsimsozder.R

/**
 * Created by azatserzhanov on 13.12.15.
 */
class CustomListAdapter(
    private var activity: Activity,
    private val id: Int,
    private val items: Array<String>
) : ArrayAdapter<String>(activity, id, items) {
    private var typeface: Typeface? = Typeface.createFromAsset(context.getAssets(), "font/kz_r.ttf");

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
    }
}