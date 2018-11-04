package kz.tilsimsozder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.json.parseValue
import kotlinx.android.synthetic.main.activity_main.*
import kz.tilsimsozder.style.CustomListAdapter
import android.util.DisplayMetrics


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getJSONData()
        getJSONLugat()
        setupAdapter()
        seekBarSetup()
        setNotes()
    }

    private fun setupAdapter() {
        val adapter = CustomListAdapter(this@MainActivity, R.layout.custom_list, LIST_TITLE_DATA.toTypedArray())
        listViewMainScreen.adapter = adapter
        listClickAction()
    }

    private fun listClickAction() {
        listViewMainScreen.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            TextViewHeader.text = LIST_TITLE_DATA[position].toUpperCase() + ""
            TextViewContent.text = LIST_CONTENT_DATA[position] + ""
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
        if (POSITION != LIST_TITLE_DATA.size) {
            TextViewHeader.text = LIST_TITLE_DATA[POSITION].toUpperCase() + ""
            TextViewContent.text = LIST_CONTENT_DATA[POSITION] + ""
            POSITION++

            setNotes()
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

    private fun getJSONData() {
        val file = applicationContext.assets.open("data.json").bufferedReader().use {
            it.readText()
        }
        val parser = JSONParser.default
        val parseJson = parser.parseValue(file)
        val parseList: List<Map<String, String>> = parseJson as List<Map<String, String>>

        val titleInJSON = "title"
        val contentInJSON = "content"

        parseList.forEach { map ->
            map.forEach {
                when (it.key) {
                    titleInJSON -> LIST_TITLE_DATA.add(it.value)
                    contentInJSON -> LIST_CONTENT_DATA.add(it.value)
                }
            }
        }
    }

    private fun getJSONLugat() {
        val file = applicationContext.assets.open("lugat.json").bufferedReader().use {
            it.readText()
        }
        val parser = JSONParser.default
        val parseJson = parser.parseValue(file)
        val parseList: List<Map<String, String>> = parseJson as List<Map<String, String>>

        val titleInJSON = "title"
        val contentInJSON = "value"

        parseList.forEach { map ->
            map.forEach {
                when (it.key) {
                    titleInJSON -> LIST_TITLE_NOTES.add(it.value)
                    contentInJSON -> LIST_CONTENT_NOTES.add(it.value)
                }
            }
        }
    }

    private fun setNotes() {
        val data: MutableList<String> = TextViewContent.text.split(" ").toMutableList()
        val pattern = "[*]".toRegex()

        TextViewContent.append("\n\n\n")
        data.forEach { data ->
            if (pattern.containsMatchIn(data)) {
                var count = 0
                LIST_TITLE_NOTES.forEach { title ->
                    if (data.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                        TextViewContent.append("$title - " + LIST_CONTENT_NOTES[count])
                    }
                    count++
                }
            }
        }
    }

    private fun seekBarSetup() {
        seekBarMain.progress = 30
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels

        scrollViewMain.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollViewMain.scrollY
            val allYSize = scrollViewMain.getChildAt(0).height - displayHeight

            if (allYSize > 0) {
                seekBarMain.progress = scrollY * 100 / allYSize
                seekBarMain.visibility = View.VISIBLE
            } else {
                seekBarMain.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        var POSITION: Int = 0
        var TEXT_SIZE = 15
        var LIST_TITLE_DATA: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA: MutableList<String> = mutableListOf()
        var LIST_TITLE_NOTES: MutableList<String> = mutableListOf()
        var LIST_CONTENT_NOTES: MutableList<String> = mutableListOf()
    }
}