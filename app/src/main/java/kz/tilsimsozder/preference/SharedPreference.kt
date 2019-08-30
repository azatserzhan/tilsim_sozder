package kz.tilsimsozder.preference

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_KEY_IS_TILSIM_PAGE = "PREF_KEY_IS_TILSIM_PAGE"
private const val PREF_KEY_IS_DARK_THEME = "PREF_KEY_IS_DARK_THEME"
private const val PREF_KEY_CURRENT_FRAGMENT_NAME = "PREF_KEY_CURRENT_FRAGMENT_NAME"

class SharedPreference(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setIsTilsimPage(state: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_TILSIM_PAGE, state)
        editor.apply()
    }

    fun getIsTilsimPage(): Boolean = prefs.getBoolean(PREF_KEY_IS_TILSIM_PAGE, false)

    fun setTheme(isDark: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_DARK_THEME, isDark)
        editor.apply()
    }

    fun getIsThemeDark(): Boolean = prefs.getBoolean(PREF_KEY_IS_DARK_THEME, false)

    fun setCurrentFragmentName(name: Int) {
        val editor = prefs.edit()
        editor.putInt(PREF_KEY_CURRENT_FRAGMENT_NAME, name)
        editor.apply()
    }

    fun getCurrentFragmentName(): Int = prefs.getInt(PREF_KEY_CURRENT_FRAGMENT_NAME, 0)
}

enum class FragmentName {
    PRAYER, TILSIM, NEWS, BOT
}