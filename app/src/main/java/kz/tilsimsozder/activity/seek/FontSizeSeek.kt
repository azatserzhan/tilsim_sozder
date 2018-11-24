package kz.tilsimsozder.activity.seek

import android.annotation.SuppressLint
import android.widget.SeekBar
import android.widget.TextView
import kz.tilsimsozder.activity.main.MainActivity

class FontSizeSeek {
    @SuppressLint("NewApi")
    fun setup(seekBar: SeekBar, textView: TextView) {
        seekBar.progress = MainActivity.TEXT_SIZE
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                if (progress > 13) {
                    MainActivity.TEXT_SIZE = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                textView.textSize = MainActivity.TEXT_SIZE.toFloat()
            }
        })
    }
}