package kz.tilsimsozder

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.menuRecyclerView
import kotlinx.android.synthetic.main.main_header.mainHeaderTextView
import kotlinx.android.synthetic.main.main_header.settingsImageView
import kotlinx.android.synthetic.main.settings_language.kazakhLanguage
import kotlinx.android.synthetic.main.settings_language.russianLanguage
import kotlinx.android.synthetic.main.settings_language.settingsLanguageContainer
import kotlinx.android.synthetic.main.settings_language.uzbekLanguage
import kz.tilsimsozder.common.BaseActivity
import kz.tilsimsozder.prayers.model.SettingsItem
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.settings.ui.SettingsAdapter
import java.util.Locale

class SettingsActivity : BaseActivity() {

    private var settingsAdapter: SettingsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.setTheme(
            if (SharedPreference(this).getIsThemeDark()) {
                R.style.CustomThemeDark
            } else {
                R.style.CustomThemeLight
            })
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        settingsAdapter = SettingsAdapter(
            menuItemListener = { openMenu(it) }
        )

        val menuManager = LinearLayoutManager(this)
        menuRecyclerView.apply {
            layoutManager = menuManager
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
        mainHeaderTextView.text = getString(R.string.settings_title)
        settingsImageView.isVisible = false

        setupLanguages()
        SharedPreference(baseContext).setIsTilsimPage(true)
    }

    override fun onBackPressed() {
        finish()
        val intent = Intent(this, TilsimSozderActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    override fun attachBaseContext(newBase: Context) {
        val updatedContext = getLocalizedContext(newBase)
        super.attachBaseContext(updatedContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreference(baseContext).setIsTilsimPage(false)
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

    private fun openMenu(position: Int) {
        when (position) {
            0 -> {
                val isDarkTheme = SharedPreference(this).getIsThemeDark()
                SharedPreference(this).setTheme(!isDarkTheme)
                this.onBackPressed()
            }
            1 -> {
                settingsLanguageContainer.isVisible = true
            }
        }
    }

    private fun setupLanguages(){
        settingsLanguageContainer.setOnClickListener {
            settingsLanguageContainer.isVisible = false
            onBackPressed()
        }

        kazakhLanguage.setOnClickListener {
            SharedPreference(this).setLanguage(0)
            onBackPressed()
        }

        russianLanguage.setOnClickListener {
            SharedPreference(this).setLanguage(1)
            onBackPressed()
        }

        uzbekLanguage.setOnClickListener {
            SharedPreference(this).setLanguage(2)
            onBackPressed()
        }

    }
}
