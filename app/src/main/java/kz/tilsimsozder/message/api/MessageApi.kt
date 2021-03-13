package kz.tilsimsozder.message.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage

class MessageApi(
    private val context: Context,
    private val preference: SharedPreference) {

    fun getMessage(): List<Prayer> {
        val fileName = when (preference.getLanguageCode()) {
            SupportLanguage.KZ.code -> "message_kz.json"
            SupportLanguage.RU.code -> "message_ru.json"
            SupportLanguage.UZ.code -> "message_uz.json"
            else -> ""
        }

        val text = getAssetsJSON(fileName)
        val prayers: List<Prayer> = GsonBuilder().create()
            .fromJson(text, object : TypeToken<ArrayList<Prayer>>() {}.type)

        prayers.forEachIndexed { index, prayer ->
            prayer.id = index.toString()
        }

        return prayers
    }

    private fun getAssetsJSON(fileName: String): String {
        val json: String
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer)

        return json
    }
}