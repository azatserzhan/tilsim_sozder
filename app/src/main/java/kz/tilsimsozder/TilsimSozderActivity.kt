package kz.tilsimsozder

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import kotlinx.android.synthetic.main.tilsim_sozder_activity.nav_view
import kotlinx.android.synthetic.main.tilsim_sozder_activity.tilsimDrawerLayout
import kz.azatserzhanov.test.common.BaseActivity
import kz.tilsimsozder.bots.ui.BotFragment
import kz.tilsimsozder.news.ui.NewsFragment
import kz.tilsimsozder.prayers.ui.PrayersFragment
import kz.tilsimsozder.tilsim.ui.TilsimFragment
import kz.tilsimsozder.tilsimsozder.ui.TilsimsozderFragment

class TilsimSozderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)

        replaceFragment(TilsimFragment.create())

        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_prayer -> {
                    /*setupSelectPrayerAdapter()
                    TextViewHeader.text = resources.getString(R.string.prayer_title)
                    TextViewContent.text = resources.getString(R.string.prayer_content)
                    prayerRecyclerView.visibility = View.VISIBLE
                    cardStackRelativeLayout.visibility = View.GONE
                    analytics.openPrayerPage()

                    hideNews()
                    hideBot()
                    Log.d("azat", "123")
                    replaceFragment(PrayersFragment.create())*/
                    replaceFragment(PrayersFragment.create())

                }
                R.id.nav_tilsim_sozder -> {
                    /*// setup(tilsimsTitle, tilsimsBidy)
                    TextViewHeader.text = resources.getString(R.string.tilsim_sozder_title)
                    // TextViewContent.text = resources.getString(R.string.tilsim_sozder_content)
                    prayerRecyclerView.visibility = View.GONE
                    cardStackRelativeLayout.visibility = View.VISIBLE
                    analytics.openTilsimPage()

                    hideNews()
                    hideBot()*/
                    replaceFragment(TilsimFragment.create())
                }
                R.id.nav_news -> {
                    replaceFragment(NewsFragment.create())
                }
                R.id.nav_bots -> {
                    replaceFragment(BotFragment.create())
                }
                /*R.id.nav_share -> {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kz.tilsimsozder")
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                    analytics.shareTilsim(tilsimsTitle[position])
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
                }*/
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
        addFragment(TilsimsozderFragment.newInstance())
    }

    override fun onBackPressed() {
        if (tilsimDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            tilsimDrawerLayout.closeDrawer(GravityCompat.START)
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
}
