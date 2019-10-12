package kz.tilsimsozder.preference

interface PreferenceContract {
    fun setIsTilsimPage(state: Boolean)
    fun getIsTilsimPage(): Boolean
    fun setTheme(isDark: Boolean)
    fun getIsThemeDark(): Boolean
    fun setCurrentFragmentName(name: Int)
    fun setFavourites(favouriteIds: List<String>)
    fun getFavourites(): MutableSet<String>?
    fun setLanguage(code: Int)
    fun getLanguageCode(): Int
    fun getLanguageStrCode(): String
}