package kz.tilsimsozder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.menuRecyclerView
import kotlinx.android.synthetic.main.main_header.mainHeaderTextView
import kotlinx.android.synthetic.main.main_header.settingsImageView
import kz.tilsimsozder.prayers.model.SettingsItem
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.settings.adapter.SettingsAdapter

class SettingsActivity : AppCompatActivity() {

    private var settingsAdapter: SettingsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsAdapter = SettingsAdapter(
            menuItemListener = { openMenu(it) }
        )

        val manager = LinearLayoutManager(this)
        menuRecyclerView.apply {
            layoutManager = manager
            adapter = settingsAdapter
        }

        val isDarkTheme = SharedPreference(this).getIsThemeDark()
        val items = listOf(
            SettingsItem(getString(R.string.settings_night), R.drawable.ic_night, true, isDarkTheme),
            SettingsItem(getString(R.string.settings_language), R.drawable.ic_language, false),
            SettingsItem(getString(R.string.settings_share), R.drawable.ic_share_blue, false),
            SettingsItem(getString(R.string.settings_feature), R.drawable.ic_telegram, false),
            SettingsItem(getString(R.string.settings_play_market), R.drawable.ic_google_play, false)
        )

        settingsAdapter?.addItems(items)
        mainHeaderTextView.text = "Баптау"
        settingsImageView.isVisible = false
    }

    private fun openMenu(position: Int) {
        when (position) {
            0 -> {
                val isDarkTheme = SharedPreference(this).getIsThemeDark()
                SharedPreference(this).setTheme(!isDarkTheme)
                this.onBackPressed()
                val intent = Intent(this, TilsimSozderActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
