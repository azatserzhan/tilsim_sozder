package kz.tilsimsozder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import kz.tilsimsozder.style.CustomListAdapter

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupAdapter()
    }

    private fun setupAdapter() {
        val adapter = CustomListAdapter(this@MainActivity, R.layout.custom_list, resources.getStringArray(R.array.prayer_title))
        listViewMainScreen.adapter = adapter
        listClickAction()
    }

    private fun listClickAction() {
        val listHeader = resources.getStringArray(R.array.prayer_title)
        val listContext = resources.getStringArray(R.array.prayer_value)

        listViewMainScreen.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            TextViewHeader.text = listHeader[position].toUpperCase() + ""
            TextViewContent.text = listContext[position] + ""
            slidingDrawer.animateClose()
            MainActivity.POSITION = position
        }
    }

    fun zoomText(view: View) {
        if (TEXT_SIZE <= 30) {
            TEXT_SIZE++
            TextViewContent.textSize = TEXT_SIZE.toFloat()
        } else {
            TEXT_SIZE = 15
        }
    }

    fun nextText(view: View) {
        val listHeader = resources.getStringArray(R.array.prayer_title)
        val listContext = resources.getStringArray(R.array.prayer_value)

        if (POSITION != listHeader.size) {
            TextViewHeader.text = listHeader[POSITION].toUpperCase() + ""
            TextViewContent.text = listContext[POSITION] + ""
            POSITION++
        } else {
            POSITION = 0
        }
    }


    fun share(view: View) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, TextViewHeader.text.toString() + "\n" + TextViewContent.text)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }


    companion object {
        var POSITION: Int = 0
        var TEXT_SIZE = 15
    }
}