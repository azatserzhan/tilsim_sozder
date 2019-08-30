package kz.tilsimsozder.tilsimsozder.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tapadoo.alerter.Alerter
import com.xw.repo.BubbleSeekBar
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_change_tilsim.*
import kz.tilsimsozder.R
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.bots.model.Bot
import kz.tilsimsozder.tilsimsozder.model.Prayer
import kz.tilsimsozder.tilsimsozder.adapter.BotAdapter
import kz.tilsimsozder.tilsimsozder.adapter.CardStackAdapter
import kz.tilsimsozder.tilsimsozder.adapter.SelectPrayerAdapter

class TilsimsozderFragment : Fragment(), CardStackListener {

    companion object {
        fun create() = TilsimsozderFragment()

        private const val CARD_VISIBLE_ITEM_COUNT = 3
        private const val CARD_TRANSLATION_INTERVAL = 4.0f
        private const val CARD_SCALE_INTERVAL = 0.95f
        private const val CARD_SWIPE_THRESHOLD = 0.3f
        private const val CARD_MAX_DEGREE = -30.0f
    }

    private var position: Int = 0

    private var tilsimsTitle: MutableList<String> = mutableListOf()
    private var tilsimsBidy: MutableList<String> = mutableListOf()

    private var prayersTitle: MutableList<String> = mutableListOf()
    private var prayersBody: MutableList<String> = mutableListOf()

    private var notesTitle: MutableList<String> = mutableListOf()
    private var notesBody: MutableList<String> = mutableListOf()
    private var startNotification = true

    private var prayerList = listOf<Prayer>()
    private val analytics = Analytics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tilsim_sozder_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupNews()
        setupBot()

        setupService()
        context?.let { analytics.setup(it) }
        analytics.openTilsimPage()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
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
        // setup(MainActivity.tilsimsTitle, MainActivity.tilsimsBidy)

        seekBarSetup()
        setNotes()
        setupStartNotification()
        pushNotificationNumber()
        setupCardStackView()
        setupButton()
        bubbleSeekBarSetup()
        // nav_view.setNavigationItemSelectedListener(this)
        // drawer_layout.openDrawer(Gravity.LEFT)
    }

    private fun pushNotificationNumber() {
        if (TilsimService.RANDOM_TILSIM != 0) {
            TextViewHeader.text = tilsimsTitle[TilsimService.RANDOM_TILSIM]
            TextViewContent.text = tilsimsBidy[TilsimService.RANDOM_TILSIM]
        }
    }

    private fun setupSelectPrayerAdapter() {
        val selectPrayerAdapter = SelectPrayerAdapter(
                clickListener = {
                    selectPrayer(it)
                }
        )
        val selectPrayerManager = LinearLayoutManager(context)
        prayerListRecyclerView.apply {
            layoutManager = selectPrayerManager
            adapter = selectPrayerAdapter
        }

        prayerList = prayersTitle
                .map { Prayer(it, "") }
                .toList()
                .apply {
                    this.forEachIndexed { index, tilsimsoz ->
                        tilsimsoz.content = prayersBody[index]
                    }
                }

        selectPrayerAdapter.addItems(prayerList)
    }

    @SuppressLint("SetTextI18n")
    private fun selectPrayer(position: Int) {
        TextViewHeader.text = prayerList[position].title.toUpperCase()
        TextViewContent.text = prayerList[position].content
        slidingDrawer.animateClose()
        this.position = position
        setNotes()
        analytics.showPrayer(prayersTitle[position])
    }

    private fun bubbleSeekBarSetup() {
        changeTilsimBubbleSeekBar.configBuilder
                .max(tilsimsTitle.size.toFloat())
                .progress(position.toFloat())
                .build()

        changeTilsimBubbleSeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
                Log.d("azat OnActionUp", progress.toString())
                changeTilsimLinearLayout.visibility = View.GONE
            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                Log.d("azat OnFinally", progress.toString())
            }

            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                cardAdapter.setPosition(progress)
            }
        }

        changeTilsimLinearLayout.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    private fun getJSONData() {
        /*TODO: Все такуи попробуй JSON прочитать, на данный момент ты XML используешь для хранения данных*/
        tilsimsTitle = this.resources.getStringArray(R.array.tilsim_sozder_title).toMutableList()
        tilsimsBidy = this.resources.getStringArray(R.array.tilsim_sozder_content).toMutableList()

        prayersTitle = this.resources.getStringArray(R.array.prayer_name).toMutableList()
        prayersBody = this.resources.getStringArray(R.array.prayer_value).toMutableList()
    }

    private fun getJSONLugat() {
        notesTitle = this.resources.getStringArray(R.array.notes_title).toMutableList()
        notesBody = this.resources.getStringArray(R.array.notes_value).toMutableList()
    }

    private fun setNotes() {
        val data: MutableList<String> = TextViewContent.text.split(" ").toMutableList()
        val pattern = "[*]".toRegex()

        TextViewContent.append("\n\n\n")
        data.forEach { text ->
            if (pattern.containsMatchIn(text)) {
                var count = 0
                notesTitle.forEach { title ->
                    if (text.substringBeforeLast("*").toLowerCase() == title.toLowerCase()) {
                        TextViewContent.append("$title - " + notesBody[count])
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

        /*scrollViewMain.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollViewMain.scrollY
            val allYSize = scrollViewMain.getChildAt(0).height - displayHeight

            if (allYSize > 0) {
                seekBarMain.progress = scrollY * 100 / allYSize
                seekBarMain.visibility = View.VISIBLE
            } else {
                seekBarMain.visibility = View.INVISIBLE
            }
        }*/
    }

    private fun setupStartNotification() {
        if (startNotification) {
            Alerter.create(activity)
                    .setTitle("Бұл жолғы жаңартылымдар")
                    .setText("Жаңалықтар бөлімі қосылды")
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
        analytics.showTilsim()
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
    private val cardAdapter by lazy {
        CardStackAdapter(tilsimsTitle, tilsimsBidy, changeTilsim())
    }

    private fun changeTilsim(): (position: Int) -> Unit = {
        changeTilsimLinearLayout.visibility = View.VISIBLE
    }

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
        activity?.stopService(service)
        activity?.startService(service)
        context?.let { SharedPreference(it).setIsTilsimPage(true) }
    }

    private fun setupButton() {
        randomButton.setOnClickListener {
            cardAdapter.setRandomPosition()
            analytics.randomButtonClicked()
            changeTilsimBubbleSeekBar.setProgress(TilsimService.RANDOM_TILSIM.toFloat())
        }

        shareImageView.setOnClickListener {
            val urlApp = "https://play.google.com/store/apps/details?id=kz.tilsimsozder"
            val sendIntent = Intent()

            analytics.sharePrayer(prayersTitle[position])

            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, urlApp + "\n\n" + TextViewHeader.text.toString() + "\n" + TextViewContent.text)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        prayerNextImageButton.setOnClickListener {
            if (position < prayersBody.size - 1) {
                position++
            }

            TextViewHeader.text = prayersTitle[position]
            TextViewContent.text = prayersBody[position]

            analytics.showPrayer(prayersTitle[position])
        }

        prayerBackImageButton.setOnClickListener {
            if (position > 0) {
                position--
            }

            TextViewHeader.text = prayersTitle[position]
            TextViewContent.text = prayersBody[position]

            analytics.showPrayer(prayersTitle[position])
        }
    }

    /*NEWS*/
    private fun setupNews() {
        newsWebView.loadUrl("http://bahai.kz/?page_id=19&lang=kk")
        newsWebView.settings.javaScriptEnabled = true
        newsWebView.webViewClient = WebViewClient()
    }

    private fun showNews() {
        newsWebView.visibility = View.VISIBLE
    }

    private fun hideNews() {
        newsWebView.visibility = View.GONE
    }

    /*BOT*/
    private lateinit var botAdapter: BotAdapter

    private fun setupBot() {
        botAdapter = BotAdapter(
                clickListener = {
                    openBot(botAdapter.getItem(it))
                }
        )

        var botManager = FlexboxLayoutManager(context)
        botManager.flexDirection = FlexDirection.ROW
        botManager.justifyContent = JustifyContent.CENTER

        botRecyclerView.apply {
            layoutManager = botManager
            adapter = botAdapter
        }


        botAdapter.addItems(listOf(
                Bot("Бот: Астана", "https://t.me/Astana_bahai_bot", R.drawable.astana_bot),
                Bot("Бот: Алматы", "https://t.me/bahai_almaty_bot", R.drawable.almaty_bot),
                Bot("YouTube: Бахаи Казахстана", "https://www.youtube.com/channel/UCSOVNuKVx_HovSbTpRZnt3Q", R.drawable.youtube_bahai_channel),
                Bot("Telegram: Медиа Канал Бахаи", "https://t.me/mediabahai", R.drawable.bot),
                Bot("Telegram: Цитаты Бахаи", "https://t.me/bahaiwisdom", R.drawable.bot),
                Bot("Instagram: kazakhstan_bahai", "https://www.instagram.com/kazakhstan_bahai/", R.drawable.insta_bahai_channel)
            ))
    }

    private fun openBot(bot: Bot) {
        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse(bot.url))
        startActivity(telegram)
    }

    private fun showBot() {
        newsWebView.visibility = View.GONE
        botRecyclerView.visibility = View.VISIBLE
    }

    private fun hideBot() {
        botRecyclerView.visibility = View.GONE
    }
}
