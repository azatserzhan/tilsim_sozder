package kz.tilsimsozder

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.setTheme(
            if (SharedPreference(this).getIsThemeDark()) {
                R.style.CustomThemeDark
            } else {
                R.style.CustomThemeLight
            })
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)

        SharedPreference(baseContext).setIsTilsimPage(true)
        // SharedPreference(baseContext).setTheme(false)
        setupService()

        // setupStyle()
        setupViewPager()
        setupIconMenu(PRAYER_PAGE_ID)
        setupHeader(PRAYER_PAGE_ID)

        settingsImageView.setOnClickListener {
            finish()
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

    private fun setupService() {
        val service = Intent(baseContext, TilsimService::class.java)
        stopService(service)
        startService(service)
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
