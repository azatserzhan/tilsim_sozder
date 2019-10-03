package kz.tilsimsozder.prayers.model

import androidx.annotation.DrawableRes

data class SettingsItem(
    val title: String,
    @DrawableRes val icon: Int,
    var isSwitchVisible: Boolean = false,
    var isDarkTheme: Boolean = false
)