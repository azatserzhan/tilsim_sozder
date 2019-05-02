package kz.tilsimsozder.tilsimsozder

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_KEY_IS_TILSIM_PAGE = "pref_key_is_tilsim_page"

class SharedPreference(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setIsTilsimPage(state: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_TILSIM_PAGE, state)
        editor.apply()
    }

    fun getIsTilsimPage(): Boolean = prefs.getBoolean(PREF_KEY_IS_TILSIM_PAGE, false)
}