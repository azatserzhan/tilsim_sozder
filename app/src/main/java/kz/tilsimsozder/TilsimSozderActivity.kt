package kz.tilsimsozder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.tilsim_sozder_activity.nav_view
import kotlinx.android.synthetic.main.tilsim_sozder_activity.tilsimDrawerLayout
import kz.azatserzhanov.test.common.BaseActivity
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.preference.FragmentName
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.tilsim.ui.TilsimFragment

class TilsimSozderActivity : BaseActivity() {

    private val analytics = Analytics()
    private var isThemeDark: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)

        when (SharedPreference(baseContext).getCurrentFragmentName()){
            FragmentName.PRAYER.ordinal -> replaceFragment(PrayersFragment.create())
            FragmentName.TILSIM.ordinal -> replaceFragment(TilsimFragment.create())
            FragmentName.BOT.ordinal -> replaceFragment(BotFragment.create())
            FragmentName.NEWS.ordinal -> replaceFragment(NewsFragment.create())
            else -> replaceFragment(TilsimFragment.create())
        }

        isThemeDark = SharedPreference(this).getIsThemeDark()
        setupStyle()
        setupNavMenu()
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)

        // setSupportActionBar(toolbar)
        tilsimDrawerLayout.openDrawer(Gravity.LEFT)
        SharedPreference(baseContext).setIsTilsimPage(true)
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

    private fun setupNavMenu(){
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

                    finish()
                    startActivity(intent)
                }
            }
            tilsimDrawerLayout.closeDrawer(GravityCompat.START)
            item.isChecked = true
            true

        }
    }
}
