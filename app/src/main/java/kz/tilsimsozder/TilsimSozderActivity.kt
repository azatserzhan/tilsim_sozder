package kz.tilsimsozder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import kotlinx.android.synthetic.main.tilsim_sozder_activity.nav_view
import kotlinx.android.synthetic.main.tilsim_sozder_activity.tilsimDrawerLayout
import kz.azatserzhanov.test.common.BaseActivity
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.firebase.Analytics
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.tilsim.ui.TilsimFragment
import kz.tilsimsozder.tilsimsozder.ui.TilsimsozderFragment

class TilsimSozderActivity : BaseActivity() {

    private val analytics = Analytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)

        replaceFragment(TilsimFragment.create())

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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kz.tilsimsozder")
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
            }
            tilsimDrawerLayout.closeDrawer(GravityCompat.START)
            item.isChecked = true
            true

        }
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)

        setSupportActionBar(toolbar)
        tilsimDrawerLayout.openDrawer(Gravity.LEFT)
        SharedPreference(baseContext).setIsTilsimPage(true)
        addFragment(TilsimsozderFragment.newInstance())
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
}
