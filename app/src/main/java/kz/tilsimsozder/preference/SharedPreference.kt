package kz.tilsimsozder.preference

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_KEY_IS_TILSIM_PAGE = "PREF_KEY_IS_TILSIM_PAGE"
private const val PREF_KEY_IS_DARK_THEME = "PREF_KEY_IS_DARK_THEME"
private const val PREF_KEY_CURRENT_FRAGMENT_NAME = "PREF_KEY_CURRENT_FRAGMENT_NAME"
private const val PREF_KEY_FAVOURITE_IDS = "PREF_KEY_FAVOURITE_IDS"
private const val PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE"
private const val PREF_KEY_IS_LANGUAGE_DIALOG_SHOWN = "PREF_KEY_IS_LANGUAGE_DIALOG_SHOWN"

class SharedPreference(context: Context) : PreferenceContract {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    override fun setIsTilsimPage(state: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_TILSIM_PAGE, state)
        editor.apply()
    }

    override fun getIsTilsimPage(): Boolean = prefs.getBoolean(PREF_KEY_IS_TILSIM_PAGE, false)

    override fun setTheme(isDark: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_DARK_THEME, isDark)
        editor.apply()
    }

    override fun getIsThemeDark(): Boolean = prefs.getBoolean(PREF_KEY_IS_DARK_THEME, false)

    override fun setCurrentFragmentName(name: Int) {
        val editor = prefs.edit()
        editor.putInt(PREF_KEY_CURRENT_FRAGMENT_NAME, name)
        editor.apply()
    }

    fun getCurrentFragmentName(): Int = prefs.getInt(PREF_KEY_CURRENT_FRAGMENT_NAME, 0)

    override fun setFavourites(favouriteIds: List<String>) {
        val editor = prefs.edit()
        val set = HashSet<String>()
        set.addAll(favouriteIds)
        editor.putStringSet(PREF_KEY_FAVOURITE_IDS, set)
        editor.apply()
    }

    override fun getFavourites(): MutableSet<String>? = prefs.getStringSet(PREF_KEY_FAVOURITE_IDS, null)

    override fun setLanguage(code: Int) {
        val editor = prefs.edit()
        editor.putInt(PREF_KEY_LANGUAGE, code)
        editor.apply()
    }

    override fun getLanguageCode(): Int = prefs.getInt(PREF_KEY_LANGUAGE, 0)

    override fun getLanguageStrCode(): String =
        when (prefs.getInt(PREF_KEY_LANGUAGE, 0)) {
            SupportLanguage.KZ.code -> SupportLanguage.KZ.strCode
            SupportLanguage.RU.code -> SupportLanguage.RU.strCode
            SupportLanguage.UZ.code -> SupportLanguage.UZ.strCode
            else -> SupportLanguage.KZ.strCode
        }

    override fun getIsLanguageDialogShow(): Boolean = prefs.getBoolean(PREF_KEY_IS_LANGUAGE_DIALOG_SHOWN, false)

    override fun setLanguageDialogShow(isShow: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREF_KEY_IS_LANGUAGE_DIALOG_SHOWN, isShow)
        editor.apply()
    }
}

enum class FragmentName {
    PRAYER, TILSIM, NEWS, BOT
}

enum class SupportLanguage(val strCode: String, val code: Int) {
    KZ("kk", 0),
    RU("ru", 1),
    UZ("uz", 2)
}