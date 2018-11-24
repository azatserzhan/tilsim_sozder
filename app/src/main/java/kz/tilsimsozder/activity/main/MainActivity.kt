package kz.tilsimsozder.activity.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tilsim_sozder.TextViewContent
import kotlinx.android.synthetic.main.activity_tilsim_sozder.TextViewHeader
import kotlinx.android.synthetic.main.activity_tilsim_sozder.listViewMainScreen
import kotlinx.android.synthetic.main.activity_tilsim_sozder.scrollViewMain
import kotlinx.android.synthetic.main.activity_tilsim_sozder.seekBarMain
import kotlinx.android.synthetic.main.activity_tilsim_sozder.slidingDrawer
import kotlinx.android.synthetic.main.app_bar_main.*
import kz.tilsimsozder.R
import kz.tilsimsozder.service.MyService
import kz.tilsimsozder.style.CustomListAdapter
import kotlinx.android.synthetic.main.content_main.baptau_menu
import kotlinx.android.synthetic.main.content_main.seekBarFontSize
import kz.tilsimsozder.activity.seek.FontSizeSeek

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)
        drawer_layout.openDrawer(Gravity.LEFT)

        getJSONData()
        getJSONLugat()

        setup(LIST_TITLE_DATA_TILSIM, LIST_CONTENT_DATA_TILSIM)

        seekBarSetup()
        setNotes()
        setupStartNotification()
        setupService()
        fontSizeSeekBarSetup()
        firstStartSetup()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_prayer -> {
                setup(LIST_TITLE_DATA_PRAYER, LIST_CONTENT_DATA_PRAYER)
                TextViewHeader.text = resources.getString(R.string.prayer_title)
                TextViewContent.text = resources.getString(R.string.prayer_content)
            }
            R.id.nav_tilsim_sozder -> {
                setup(LIST_TITLE_DATA_TILSIM, LIST_CONTENT_DATA_TILSIM)
                TextViewHeader.text = resources.getString(R.string.tilsim_sozder_title)
                TextViewContent.text = resources.getString(R.string.tilsim_sozder_content)
            }
            R.id.nav_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kz.tilsimsozder")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.nav_send -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "azatserzhan@gmail.com", null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Бахаи дұғалары жайлы пікір")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Осында хатыңызды жазыңыз")
                startActivity(Intent.createChooser(emailIntent, "Хат жазу..."))
            }
            R.id.nav_manage -> {
                baptau_menu.visibility = View.VISIBLE
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun firstStartSetup() {
        if (MyService.RANDOM_TILSIM != 0) {
            TextViewHeader.text = LIST_TITLE_DATA_TILSIM[MyService.RANDOM_TILSIM]
            TextViewContent.text = LIST_CONTENT_DATA_TILSIM[MyService.RANDOM_TILSIM]
        }
    }

    private fun setup(listTitle: MutableList<String>, listData: MutableList<String>) {
        setupAdapter(listTitle, listData)
    }

    private fun setupAdapter(listTitle: MutableList<String>, listData: MutableList<String>) {
        val adapter = CustomListAdapter(this@MainActivity, R.layout.custom_list, listTitle.toTypedArray())
        listViewMainScreen.adapter = adapter
        listClickAction(listTitle, listData)
    }

    private fun setupService() {
        val service = Intent(this, MyService::class.java)
        stopService(service)
        startService(service)
    }

    private fun listClickAction(listTitle: MutableList<String>, listData: MutableList<String>) {
        listViewMainScreen.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            TextViewHeader.text = listTitle[position].toUpperCase() + ""
            TextViewContent.text = listData[position] + ""
            slidingDrawer.animateClose()
            POSITION = position
            setNotes()
        }
    }

    fun nextText(view: View) {
        if (POSITION != LIST_TITLE_DATA_TILSIM.size) {
            TextViewHeader.text = LIST_TITLE_DATA_TILSIM[POSITION].toUpperCase() + ""
            TextViewContent.text = LIST_CONTENT_DATA_TILSIM[POSITION] + ""
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

    fun baptau(view: View) {
        view.visibility = View.GONE
    }

    private fun fontSizeSeekBarSetup() {
        val seekBar = FontSizeSeek()
        seekBar.setup(seekBarFontSize, TextViewContent)
        /*seekBarFontSize.progress = TEXT_SIZE
        seekBarFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                TEXT_SIZE = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                TextViewContent.textSize = TEXT_SIZE.toFloat()
            }
        })*/
    }

    private fun getJSONData() {
        /*TODO: Все такуи попробуй JSON прочитать, на данный момент ты XML используешь для хранения данных*/
        LIST_TITLE_DATA_TILSIM = this.resources.getStringArray(R.array.tilsim_sozder_title).toMutableList()
        LIST_CONTENT_DATA_TILSIM = this.resources.getStringArray(R.array.tilsim_sozder_content).toMutableList()

        LIST_TITLE_DATA_PRAYER = this.resources.getStringArray(R.array.prayer_name).toMutableList()
        LIST_CONTENT_DATA_PRAYER = this.resources.getStringArray(R.array.prayer_value).toMutableList()

        /*val file = applicationContext.assets.open("data.json").bufferedReader().use {
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
                    titleInJSON -> LIST_TITLE_DATA_TILSIM.add(it.value)
                    contentInJSON -> LIST_CONTENT_DATA_TILSIM.add(it.value)
                }
            }
        }*/
    }

    private fun getJSONLugat() {
        LIST_TITLE_NOTES = this.resources.getStringArray(R.array.notes_title).toMutableList()
        LIST_CONTENT_NOTES = this.resources.getStringArray(R.array.notes_value).toMutableList()

        /*val file = applicationContext.assets.open("lugat.json").bufferedReader().use {
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
        }*/
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

    private fun setupStartNotification() {
        if (START_NOTIFICATION) {
            Alerter.create(this)
                .setTitle("Бұл жолығы жаңартылымдар")
                .setText("Үш сағат сайын сізге кездейсоқ тылсым сөз келіп отырады")
                .setBackgroundColorRes(R.color.colorNotification)
                .show()
        }
    }

    companion object {
        var POSITION: Int = 0
        var TEXT_SIZE = 15

        var LIST_TITLE_DATA_TILSIM: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA_TILSIM: MutableList<String> = mutableListOf()

        var LIST_TITLE_DATA_PRAYER: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA_PRAYER: MutableList<String> = mutableListOf()

        var LIST_TITLE_NOTES: MutableList<String> = mutableListOf()
        var LIST_CONTENT_NOTES: MutableList<String> = mutableListOf()
        var START_NOTIFICATION = true
    }
}
