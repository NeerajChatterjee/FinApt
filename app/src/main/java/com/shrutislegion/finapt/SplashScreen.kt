package com.shrutislegion.finapt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            // pass the value player to the next activity and then open the Home activity according to the boolean value
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra(MainActivity.EXTRA_LOGINTYPE, "$player")
            startActivity(intent)
            finish()
        },2500)
    }
}