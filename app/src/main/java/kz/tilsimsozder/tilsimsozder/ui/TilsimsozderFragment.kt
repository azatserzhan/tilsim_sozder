package kz.tilsimsozder.tilsimsozder.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.tapadoo.alerter.Alerter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.content_main.*
import kz.tilsimsozder.R
import kz.tilsimsozder.activity.seek.FontSizeSeek
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsimsozder.SharedPreference
import kz.tilsimsozder.tilsimsozder.model.Prayer
import kz.tilsimsozder.tilsimsozder.ui.adapter.CardStackAdapter
import kz.tilsimsozder.tilsimsozder.ui.adapter.SelectPrayerAdapter

class TilsimsozderFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener, CardStackListener {

    companion object {
        fun newInstance() = TilsimsozderFragment()
        var POSITION: Int = 0
        var TEXT_SIZE = 15

        var LIST_TITLE_DATA_TILSIM: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA_TILSIM: MutableList<String> = mutableListOf()

        var LIST_TITLE_DATA_PRAYER: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA_PRAYER: MutableList<String> = mutableListOf()

        var LIST_TITLE_NOTES: MutableList<String> = mutableListOf()
        var LIST_CONTENT_NOTES: MutableList<String> = mutableListOf()
        var START_NOTIFICATION = true

        private const val CARD_VISIBLE_ITEM_COUNT = 3
        private const val CARD_TRANSLATION_INTERVAL = 4.0f
        private const val CARD_SCALE_INTERVAL = 0.95f
        private const val CARD_SWIPE_THRESHOLD = 0.3f
        private const val CARD_MAX_DEGREE = -30.0f
    }

    private lateinit var viewModel: TilsimsozderViewModel
    private var prayerList = listOf<Prayer>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tilsim_sozder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TilsimsozderViewModel::class.java)
        setupView()

        setupService()
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
                setupSelectPrayerAdapter()
                TextViewHeader.text = resources.getString(R.string.prayer_title)
                TextViewContent.text = resources.getString(R.string.prayer_content)
                prayerRecyclerView.visibility = View.VISIBLE
                cardStackRelativeLayout.visibility = View.GONE
            }
            R.id.nav_tilsim_sozder -> {
                // setup(LIST_TITLE_DATA_TILSIM, LIST_CONTENT_DATA_TILSIM)
                TextViewHeader.text = resources.getString(R.string.tilsim_sozder_title)
                // TextViewContent.text = resources.getString(R.string.tilsim_sozder_content)
                prayerRecyclerView.visibility = View.GONE
                cardStackRelativeLayout.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let { SharedPreference(it).setIsTilsimPage(false) }
    }

    override fun onDestroy() {
        context?.let { SharedPreference(it).setIsTilsimPage(false) }
        super.onDestroy()
    }

    private fun setupView() {
        getJSONData()
        getJSONLugat()

        //TODO: расскрой коммент
        // setup(MainActivity.LIST_TITLE_DATA_TILSIM, MainActivity.LIST_CONTENT_DATA_TILSIM)

        seekBarSetup()
        setNotes()
        setupStartNotification()
        fontSizeSeekBarSetup()
        pushNotificationNumber()
        setupCardStackView()
        setupButton()
        nav_view.setNavigationItemSelectedListener(this)
        drawer_layout.openDrawer(Gravity.LEFT)
    }

    private fun pushNotificationNumber() {
        if (TilsimService.RANDOM_TILSIM != 0) {
            TextViewHeader.text = LIST_TITLE_DATA_TILSIM[TilsimService.RANDOM_TILSIM]
            TextViewContent.text = LIST_CONTENT_DATA_TILSIM[TilsimService.RANDOM_TILSIM]
        }
    }

    private fun setupSelectPrayerAdapter() {
        // setupAdapter(listTitle, listData)
        val selectPrayerAdapter = SelectPrayerAdapter(
                clickListener = {
                    selectPrayer(it)
                }
        )
        val selectPrayerManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        prayerListRecyclerView.apply {
            layoutManager = selectPrayerManager
            adapter = selectPrayerAdapter
        }

        prayerList = LIST_TITLE_DATA_PRAYER
                .map { Prayer(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.content = LIST_CONTENT_DATA_PRAYER[index]
                    }
                }

        selectPrayerAdapter.addItems(prayerList)
    }

    @SuppressLint("SetTextI18n")
    private fun selectPrayer(position: Int) {
        TextViewHeader.text = prayerList[position].title.toUpperCase()
        TextViewContent.text = prayerList[position].content
        slidingDrawer.animateClose()
        POSITION = position
        setNotes()
    }

    private fun setupAdapter(listTitle: MutableList<String>, listData: MutableList<String>) {
        // val adapter = SelectPrayerAdapter(activity?.parent!!, R.layout.select_prayer,
        // .toTypedArray())
        // listViewMainScreen.adapter = adapter
        // listClickAction(listTitle, listData)
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

        val titleInJSON = "titleTextView"
        val contentInJSON = "contentTextView"

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

        val titleInJSON = "titleTextView"
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
        //TODO: открой windowManager
        // windowManager.defaultDisplay.getMetrics(displayMetrics)
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
            Alerter.create(activity)
                    .setTitle("Бұл жолығы жаңартылымдар")
                    .setText("Кездейсоқ батырмасы қосылды. Карточкаларды төменге қарай тарту арқылы өзгертуге болады")
                    .setBackgroundColorRes(R.color.colorNotification)
                    .show()
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d("azat", "setupStartNotification")
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("azat", "onCardDragging")
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d("azat", "onCardSwiped")
    }

    override fun onCardCanceled() {
        Log.d("azat", "onCardCanceled")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d("azat", "onCardAppeared")
    }

    override fun onCardRewound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val cardManager by lazy { CardStackLayoutManager(activity) }
    private val cardAdapter by lazy { CardStackAdapter(LIST_TITLE_DATA_TILSIM, LIST_CONTENT_DATA_TILSIM) }

    private fun setupCardStackView() {
        cardManager.setStackFrom(StackFrom.Top)
        cardManager.setVisibleCount(CARD_VISIBLE_ITEM_COUNT)
        cardManager.setTranslationInterval(CARD_TRANSLATION_INTERVAL)
        cardManager.setScaleInterval(CARD_SCALE_INTERVAL)
        cardManager.setSwipeThreshold(CARD_SWIPE_THRESHOLD)
        cardManager.setMaxDegree(CARD_MAX_DEGREE)
        cardManager.setDirections(Direction.FREEDOM)
        cardManager.setCanScrollHorizontal(true)
        cardManager.setCanScrollVertical(true)
        card_stack_view.layoutManager = cardManager
        card_stack_view.adapter = cardAdapter
        card_stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = true
            }
        }
    }

    private fun setupService() {
        val service = Intent(activity, TilsimService::class.java)
        // bindService(service, connection, Context.BIND_AUTO_CREATE)
        activity?.stopService(service)
        activity?.startService(service)
        context?.let { SharedPreference(it).setIsTilsimPage(true) }
    }

    private fun setupButton() {
        randomButton.setOnClickListener {
            cardAdapter.setNewPosition()
        }

        shareImageView.setOnClickListener {
            val urlApp = "https://play.google.com/store/apps/details?id=kz.tilsimsozder"
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, urlApp + "\n\n" + TextViewHeader.text.toString() + "\n" + TextViewContent.text)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        prayerNextImageButton.setOnClickListener {
            if (POSITION < LIST_CONTENT_DATA_PRAYER.size - 1) {
                POSITION++
            }

            TextViewHeader.text = LIST_TITLE_DATA_PRAYER[POSITION]
            TextViewContent.text = LIST_CONTENT_DATA_PRAYER[POSITION]
        }

        prayerBackImageButton.setOnClickListener {
            if (POSITION > 0) {
                POSITION--
            }

            TextViewHeader.text = LIST_TITLE_DATA_PRAYER[POSITION]
            TextViewContent.text = LIST_CONTENT_DATA_PRAYER[POSITION]
        }
    }
}
