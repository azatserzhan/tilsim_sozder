package kz.tilsimsozder.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

private const val SHOW_TILSIM_COUNT = "SHOW_TILSIM_COUNT"
class Analytics {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun setup(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun sendShareTilsim(contentType: String, itemId: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
    }

    fun showTilsim() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(SHOW_TILSIM_COUNT, bundle)
    }
}