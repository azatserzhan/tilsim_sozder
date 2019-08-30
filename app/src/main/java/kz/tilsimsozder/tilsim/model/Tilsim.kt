package kz.tilsimsozder.tilsim.model

private var newPosition = 0

data class Tilsim(
        val title: String,
        var body: String,
        var note: String = "",
        var position: Int = newPosition++
)