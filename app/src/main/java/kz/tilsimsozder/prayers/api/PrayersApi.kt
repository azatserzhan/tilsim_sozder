package kz.tilsimsozder.prayers.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kz.tilsimsozder.prayers.model.Prayer
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage
import kz.tilsimsozder.tilsim.model.Tilsim

class PrayersApi(
    private val context: Context,
    private val preference: SharedPreference) {

    fun getPrayers(): List<Prayer> {
        val fileName = when (preference.getLanguageCode()) {
            SupportLanguage.KZ.code -> "prayer_kz.json"
            SupportLanguage.RU.code -> "prayer_ru.json"
            SupportLanguage.UZ.code -> "prayer_uz.json"
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

    fun getTilsimList(): List<Tilsim> {
        val fileName = when (preference.getLanguageCode()) {
            SupportLanguage.KZ.code -> "tilsim_kz.json"
            SupportLanguage.RU.code -> "tilsim_ru.json"
            SupportLanguage.UZ.code -> "tilsim_uz.json"
            else -> ""
        }

        val text = getAssetsJSON(fileName)
        val tilsimList: List<Tilsim> = GsonBuilder().create()
            .fromJson(text, object : TypeToken<ArrayList<Tilsim>>() {}.type)

        tilsimList.forEachIndexed { index, tilsim ->
            tilsim.position = index
        }

        return tilsimList
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