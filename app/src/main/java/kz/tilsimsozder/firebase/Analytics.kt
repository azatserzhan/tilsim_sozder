package kz.tilsimsozder.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

private const val SHOW_TILSIM_COUNT = "show_tilsim_count"
private const val SHARE_TILSIM = "share_tilsim"
private const val OPEN_TILSIM_PAGE_COUNT = "open_tilsim_page_count"
private const val RANDOM_TILSIM_BUTTON_CLICKED = "random_tilsim_button_clicked"
private const val OPEN_PRAYER_PAGE_COUNT = "open_prayer_page_count"
private const val SHOW_PRAYER = "show_prayer"
private const val TITLE = "title"
private const val SHARE_PRAYER = "share_prayer"

class Analytics {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun setup(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun shareTilsim(prayerTitle: String) {
        val bundle = Bundle()
        bundle.putString(TITLE, prayerTitle)
        firebaseAnalytics.logEvent(SHARE_TILSIM, bundle)
    }

    fun showTilsim() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(SHOW_TILSIM_COUNT, bundle)
    }

    fun openTilsimPage() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(OPEN_TILSIM_PAGE_COUNT, bundle)
    }

    fun randomButtonClicked() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(RANDOM_TILSIM_BUTTON_CLICKED, bundle)
    }

    fun openPrayerPage() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(OPEN_PRAYER_PAGE_COUNT, bundle)
    }

    fun showPrayer(prayerTitle: String){
        val bundle = Bundle()
        bundle.putString(SHOW_PRAYER, prayerTitle)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
    }


    fun sharePrayer(prayerTitle: String) {
        val bundle = Bundle()
        bundle.putString(TITLE, prayerTitle)
        firebaseAnalytics.logEvent(SHARE_PRAYER, bundle)
    }
}