package com.shrutislegion.finapt

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import io.grpc.internal.SharedResourceHolder.Resource
import java.util.Locale

@Suppress("DEPRECATION")
class LanguageManager (val context: Context){

    val sharedPreferences = context.getSharedPreferences("Lang", Context.MODE_PRIVATE)
    fun updateResources(code: String){
        val locale: Locale = Locale(code)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        setLang(code)
    }
    fun getLang() : String? {
        return sharedPreferences.getString("lang", "en")
    }
    fun setLang(code: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("lang", code)
        editor.commit()
    }
}