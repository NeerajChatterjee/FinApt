package com.shrutislegion.finapt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

public open class AppCompat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageManager: LanguageManager = LanguageManager(this)
        languageManager.updateResources(languageManager.getLang()!!)
    }
}