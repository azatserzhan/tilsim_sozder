package kz.tilsimsozder.tilsim.model

import com.google.gson.annotations.SerializedName

data class Tilsim(
    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    var body: String,

    var note: String = "",

    var position: Int
)