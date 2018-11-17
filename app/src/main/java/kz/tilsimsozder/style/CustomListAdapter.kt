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
import android.widget.ImageView
import android.widget.TextView

import kz.tilsimsozder.R

/**
 * Created by azatserzhanov on 13.12.15.
 */
class CustomListAdapter(var mContext: Activity, private val id: Int, private val items: Array<String>)//tr = Typeface.createFromAsset(context.getAssets(), "font/kz_r.ttf");
    : ArrayAdapter<String>(mContext, id, items) {
    private var tl: Typeface? = Typeface.createFromAsset(context.getAssets(), "font/kz_r.ttf");

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var mView = v
        if (mView == null) {
            val vi = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mView = vi.inflate(id, null)
        }

        val text = mView!!.findViewById<View>(R.id.textView) as TextView

        if (items[position] != null) {
            text.text = Html.fromHtml(items[position])
            text.typeface = tl
        }
        text.setTextColor(Color.parseColor("#ffffff"))

        val textViewFirstNumber = mView!!.findViewById<View>(R.id.textViewFirstNumber) as TextView
        textViewFirstNumber.text = position.toString()


        return mView
    }

}