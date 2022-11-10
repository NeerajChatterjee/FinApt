// This opens up for a new user or not logged in user, it contains IntroSlider

package com.shrutislegion.finapt

import android.annotation.SuppressLint
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


class MainActivity : AppCompat() {
    lateinit var introSliderAdapter: IntroSliderAdapter
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.hide()
        introSliderAdapter = IntroSliderAdapter(
            listOf(
                IntroSlide(
                    resources.getString(R.string.welcome_to_finapt),
                    resources.getString(R.string.for_your_financial_fitness),
                    R.drawable.slide1
                ),
                IntroSlide(
                    resources.getString(R.string.manage_your_expenses),
                    resources.getString(R.string.get_control_over_your_money),
                    R.drawable.slide2
                ),
                IntroSlide(
                    resources.getString(R.string.track_sales_and_profits),
                    resources.getString(R.string.now_is_the_time_to_upgrade),
                    R.drawable.slide3
                )
            )
        )

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
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }
        }
        text2.setOnClickListener{

            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()

        }
        val lang = LanguageManager(this)
        hindi.setOnClickListener {
            lang.updateResources("hi")
            hindi.setTextColor(R.color.color_primary)
            recreate()
        }
        english.setOnClickListener {
            lang.updateResources("en")
            english.setTextColor(R.color.color_primary)
            recreate()
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