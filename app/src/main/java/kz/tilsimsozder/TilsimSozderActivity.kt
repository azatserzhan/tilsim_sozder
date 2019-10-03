package kz.tilsimsozder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.main_header.mainHeaderTextView
import kotlinx.android.synthetic.main.main_header.settingsImageView
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemNews
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemPrayer
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemService
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemTilsim
import kotlinx.android.synthetic.main.tilsim_sozder_activity.nav_view
import kotlinx.android.synthetic.main.tilsim_sozder_activity.tilsimDrawerLayout
import kotlinx.android.synthetic.main.tilsim_sozder_activity.viewPager
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.common.BaseActivity
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.ui.TilsimFragment

private const val PRAYER_PAGE_ID = 0
private const val TILSIM_PAGE_ID = 1
private const val NEWS_PAGE_ID = 2
private const val SERVICE_PAGE_ID = 3

class TilsimSozderActivity : BaseActivity() {

    private val analytics = Analytics()
    private var isThemeDark: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)

        SharedPreference(baseContext).setIsTilsimPage(true)
        // SharedPreference(baseContext).setTheme(false)
        setupService()

        isThemeDark = SharedPreference(this).getIsThemeDark()
        setupStyle()
        setupNavMenu()
        setupViewPager()
        setupIconMenu(PRAYER_PAGE_ID)
        setupHeader(PRAYER_PAGE_ID)

        settingsImageView.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (tilsimDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tilsimDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreference(baseContext).setIsTilsimPage(false)
    }

    private fun setupStyle() {
        if (isThemeDark) {
            this.setTheme(R.style.CustomThemeDark)
        } else {
            this.setTheme(R.style.CustomThemeLight)
        }
    }

    private fun setupService() {
        val service = Intent(baseContext, TilsimService::class.java)
        stopService(service)
        startService(service)
    }

    private fun setupNavMenu() {
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_prayer -> {
                    replaceFragment(PrayersFragment.create())
                }
                R.id.nav_tilsim_sozder -> {
                    replaceFragment(TilsimFragment.create())
                }
                R.id.nav_news -> {
                    replaceFragment(NewsFragment.create())
                }
                R.id.nav_bots -> {
                    replaceFragment(BotFragment.create())
                }
                R.id.nav_share -> {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=kz.tilsimsozder"
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                    analytics.shareApp()
                }
                R.id.nav_send -> {
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "azatserzhan@gmail.com", null))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Бахаи дұғалары жайлы пікір")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Осында хатыңызды жазыңыз")
                    startActivity(Intent.createChooser(emailIntent, "Хат жазу..."))
                    analytics.sendEmail()
                }
                R.id.nav_change_theme -> {
                    if (isThemeDark) {
                        SharedPreference(this.baseContext).setTheme(false)
                    } else {
                        SharedPreference(this.baseContext).setTheme(true)
                    }

                    recreate()
                }
            }
            tilsimDrawerLayout.closeDrawer(GravityCompat.START)
            item.isChecked = true
            true

        }
    }

    private fun setupViewPager() {
        if (viewPager != null) {
            val adapter = ViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageSelected(position: Int) {
                    setupIconMenu(position)
                    setupHeader(position)
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            })

            menuItemPrayer.setOnClickListener {
                viewPager.setCurrentItem(PRAYER_PAGE_ID, true)
                setupIconMenu(PRAYER_PAGE_ID)
            }
            menuItemTilsim.setOnClickListener {
                viewPager.setCurrentItem(TILSIM_PAGE_ID, true)
                setupIconMenu(TILSIM_PAGE_ID)
            }
            menuItemNews.setOnClickListener {
                viewPager.setCurrentItem(NEWS_PAGE_ID, true)
                setupIconMenu(NEWS_PAGE_ID)
            }
            menuItemService.setOnClickListener {
                viewPager.setCurrentItem(SERVICE_PAGE_ID, true)
                setupIconMenu(SERVICE_PAGE_ID)
            }
        }
    }

    private fun setupIconMenu(position: Int) {
        menuItemPrayer.background = getDrawable(R.drawable.ic_prayer)
        menuItemTilsim.background = getDrawable(R.drawable.ic_tilsim)
        menuItemNews.background = getDrawable(R.drawable.ic_news)
        menuItemService.background = getDrawable(R.drawable.ic_service)

        when (position) {
            PRAYER_PAGE_ID -> menuItemPrayer.background = getDrawable(R.drawable.ic_prayer_active)
            TILSIM_PAGE_ID -> menuItemTilsim.background = getDrawable(R.drawable.ic_tilsim_active)
            NEWS_PAGE_ID -> menuItemNews.background = getDrawable(R.drawable.ic_news_active)
            SERVICE_PAGE_ID -> menuItemService.background = getDrawable(R.drawable.ic_service_active)
        }
    }

    private fun setupHeader(position: Int) {
        when (position) {
            PRAYER_PAGE_ID -> mainHeaderTextView.text = getText(R.string.prayer_title)
            TILSIM_PAGE_ID -> mainHeaderTextView.text = getText(R.string.tilsim_sozder_title)
            NEWS_PAGE_ID -> mainHeaderTextView.text = getText(R.string.news_title)
            SERVICE_PAGE_ID -> mainHeaderTextView.text = getText(R.string.service_title)
        }
    }
}

internal class ViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    private var count = 4

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            PRAYER_PAGE_ID -> fragment = PrayersFragment.create()
            TILSIM_PAGE_ID -> fragment = TilsimFragment.create()
            NEWS_PAGE_ID -> fragment = NewsFragment.create()
            SERVICE_PAGE_ID -> fragment = BotFragment.create()
        }

        return fragment
    }

    override fun getCount(): Int {
        return count
    }
}
