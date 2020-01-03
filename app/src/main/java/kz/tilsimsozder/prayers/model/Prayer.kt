package kz.tilsimsozder.prayers.model

import com.google.gson.annotations.SerializedName

data class Prayer(
        var id: String = "",

        @SerializedName("title")
        val title: String,

        @SerializedName("content")
        var body: String,

        var isFavourite: Boolean = false
)