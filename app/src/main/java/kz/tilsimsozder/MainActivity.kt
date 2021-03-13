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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_header.mainHeaderTextView
import kotlinx.android.synthetic.main.main_header.nightModeImageView
import kotlinx.android.synthetic.main.main_header.settingsImageView
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemNews
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemPrayer
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemService
import kotlinx.android.synthetic.main.tilsim_sozder_activity.menuItemTilsim
import kotlinx.android.synthetic.main.tilsim_sozder_activity.viewPager
import kz.tilsimsozder.auth.OKAuth
import kz.tilsimsozder.auth.VKAuth
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.common.BaseActivity
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.inappupdates.InAppUpdateManager
import kz.tilsimsozder.inappupdates.UpdateState
import kz.tilsimsozder.message.ui.MessageFragment
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.service.TilsimService
import kz.tilsimsozder.tilsim.ui.TilsimFragment
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.Locale

private const val PRAYER_PAGE_ID = 0
private const val TILSIM_PAGE_ID = 1
private const val NEWS_PAGE_ID = 2
private const val SERVICE_PAGE_ID = 3

class TilsimSozderActivity : BaseActivity() {

    private val analytics = Analytics()
    private val inAppUpdateManager: InAppUpdateManager by inject()
    private var updateManagerStateDisposable: Disposable? = null

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
        checkInAppUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreference(baseContext).setIsTilsimPage(false)
        updateManagerStateDisposable?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == InAppUpdateManager.REQUEST_CODE) {
            inAppUpdateManager.onActivityResult(resultCode)
        }
        if (requestCode == VKAuth.VK_SIGN_IN || requestCode == OKAuth.OK_SIGN_IN) {
            //fragment.onActivityResult(requestCode, resultCode, data)
        }
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
        menuItemService.background = getDrawable(R.drawable.ic_message)

        when (position) {
            PRAYER_PAGE_ID -> menuItemPrayer.background = getDrawable(R.drawable.ic_prayer_active)
            TILSIM_PAGE_ID -> menuItemTilsim.background = getDrawable(R.drawable.ic_tilsim_active)
            NEWS_PAGE_ID -> menuItemNews.background = getDrawable(R.drawable.ic_news_active)
            SERVICE_PAGE_ID -> menuItemService.background = getDrawable(R.drawable.ic_message_active)
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

    private fun checkInAppUpdate() {
        updateManagerStateDisposable?.dispose()
        updateManagerStateDisposable = inAppUpdateManager.observeState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Timber.d("update: isLoading")
            }
            .subscribe(
                { state ->
                    if (state == UpdateState.READY_FOR_DOWNLOAD) {
                        inAppUpdateManager.startUpdate(this)
                        Timber.d("update: READY_FOR_DOWNLOAD")
                    }
                    inAppUpdateManager.startUpdate(this)
                },
                {
                    Timber.e(it, "update: Error to get update state")
                }
            )
        inAppUpdateManager.start()
    }
}

class ViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var count = 4

    override fun getItem(position: Int): Fragment =
        when (position) {
            PRAYER_PAGE_ID -> PrayersFragment.create()
            TILSIM_PAGE_ID -> TilsimFragment.create()
            NEWS_PAGE_ID -> NewsFragment.create()
            SERVICE_PAGE_ID -> MessageFragment.create()
            else -> PrayersFragment.create()
        }

    override fun getCount(): Int {
        return count
    }
}
