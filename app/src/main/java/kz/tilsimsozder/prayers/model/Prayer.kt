package kz.tilsimsozder.prayers.model

private var idCounter = 0

data class Prayer(
        var id: String = (idCounter ++).toString(),
        val title: String,
        var body: String,
        var isFavourite: Boolean = false
)