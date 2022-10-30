package com.shrutislegion.finapt

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.shrutislegion.finapt.IntroSlideAdapters.IntroSlide
import com.shrutislegion.finapt.IntroSlideAdapters.IntroSliderAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Welcome to FinApt",
                "Sexy Tag Line!!",
                R.drawable.slide1
            ),
            IntroSlide(
                "Manage Your Expenses",
                "Get control over your Money",
                R.drawable.slide2
            ),
            IntroSlide(
                "Track Sales and Profits",
                "Fed up of managing long tally excel sheets? Now's the time to upgrade!!",
                R.drawable.slide3
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.hide()

        introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        }
        )
        next_btn.setOnClickListener{
            if(introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                introSliderViewPager.currentItem += 1
            }
            else {
                startActivity(Intent(this, Registration_Activity::class.java))
                finish()
            }
        }
        text2.setOnClickListener{

            startActivity(Intent(this, Registration_Activity::class.java))
            finish()

        }
    }
    private fun setupIndicators(){

//        if(FirebaseAuth.getInstance().currentUser != null){
//            startActivity(Intent(this, UserDashboardActivity::class.java))
//            finish()
//            return
//        }

        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_launcher_background
//                        R.drawable.indicator_inactive

                    )
                )
                this?.layoutParams = layoutParams
            }
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if(i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            }
            else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}


//fun onClick(v: View) {
//    when (v.getId()) {
//        R.id.sign_in_button -> signIn()
//    }
//}