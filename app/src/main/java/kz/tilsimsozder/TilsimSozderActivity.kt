package kz.tilsimsozder

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.main_header.mainHeaderTextView
import kotlinx.android.synthetic.main.main_header.nightModeImageView
import kotlinx.android.synthetic.main.main_header.settingsImageView
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemNews
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemPrayer
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemService
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemTilsim
import kotlinx.android.synthetic.main.tilsim_sozder_activity.viewPager
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.common.BaseActivity
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.ui.TilsimFragment
import java.util.Locale

private const val PRAYER_PAGE_ID = 0
private const val TILSIM_PAGE_ID = 1
private const val NEWS_PAGE_ID = 2
private const val SERVICE_PAGE_ID = 3

class TilsimSozderActivity : BaseActivity() {

    private val analytics = Analytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        val isThemeDark = SharedPreference(baseContext).getIsThemeDark()
        super.setTheme(
            if (isThemeDark) {
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

        nightModeImageView.setOnClickListener {
            SharedPreference(baseContext).setTheme(!isThemeDark)
            finish()
            val intent = Intent(this, TilsimSozderActivity::class.java)
            startActivity(intent)
        }
        nightModeImageView.setBackgroundResource(if (!isThemeDark) R.drawable.ic_sunny_day else R.drawable.ic_night)

        forceUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreference(baseContext).setIsTilsimPage(false)
    }

    override fun attachBaseContext(newBase: Context) {
        val updatedContext = getLocalizedContext(newBase)
        super.attachBaseContext(updatedContext)
        updateConfigurationIfNeededAndGetLanguage(resources, updatedContext.resources)
    }

    private fun updateConfigurationIfNeededAndGetLanguage(oldResources: Resources, newResources: Resources): String =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            oldResources.updateConfiguration(newResources.configuration, newResources.displayMetrics)
            oldResources.configuration.locale.language
        } else {
            oldResources.configuration.locales[0].language
        }

    private fun getLocalizedContext(context: Context): Context {
        val languageStrCode = SharedPreference(context).getLanguageStrCode()
        val locale = Locale(languageStrCode)
        Locale.setDefault(locale)
        val configuration = Configuration()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        } else {
            configuration.locales = LocaleList(locale)
        }

        return context.createConfigurationContext(configuration)
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
            PRAYER_PAGE_ID -> mainHeaderTextView.text = getString(R.string.prayer_title)
            TILSIM_PAGE_ID -> mainHeaderTextView.text = getString(R.string.tilsim_sozder_title)
            NEWS_PAGE_ID -> mainHeaderTextView.text = getString(R.string.news_title)
            SERVICE_PAGE_ID -> mainHeaderTextView.text = getString(R.string.service_title)
        }
    }

    private fun forceUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        0
                    )
                }
            }
    }
}

class ViewPagerAdapter(
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
