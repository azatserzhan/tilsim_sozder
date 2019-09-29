package kz.tilsimsozder.prayers.model

import java.util.*

data class Prayer(
        var id: String = UUID.randomUUID().toString(),
        val title: String,
        var body: String,
        var isFavourite: Boolean = false
)