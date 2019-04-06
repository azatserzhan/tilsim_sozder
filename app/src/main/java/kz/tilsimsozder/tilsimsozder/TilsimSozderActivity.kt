package kz.tilsimsozder.tilsimsozder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsimsozder.ui.mainactivity.TilsimsozderFragment

class TilsimSozderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tilsim_sozder_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TilsimsozderFragment.newInstance())
                .commitNow()
        }
    }
}
