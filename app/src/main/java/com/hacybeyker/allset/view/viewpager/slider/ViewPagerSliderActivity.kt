package com.hacybeyker.allset.view.viewpager.slider

import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.data.MovieData
import com.hacybeyker.allset.databinding.ActivityViewPagerSliderBinding
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.viewpager.slider.adapter.SliderAdapter
import com.hacybeyker.allset.view.viewpager.slider.adapter.SliderAdapter2
import kotlin.math.abs

class ViewPagerSliderActivity : BaseActivity() {

    private lateinit var binding: ActivityViewPagerSliderBinding
    private val adapter: SliderAdapter by lazy { SliderAdapter() }
    private val adapter2: SliderAdapter2 by lazy { SliderAdapter2() }
    private val sliderHandler: Handler by lazy { Handler() }
    private lateinit var item: Item

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName
        private const val TIME = 3000L

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ViewPagerSliderActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityViewPagerSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.tvDescription.text = item.description
        setupListeners()
        setupViewPager()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun setupListeners() {
        binding.tvDescription.setOnClickListener {

        }
    }

    private fun setupViewPager() {
        /*----Resources----*/
        //Github:
        //Page:
        //References:
        //Dependencies:
        //implementation("androidx.viewpager2:viewpager2:1.0.0")
        //implementation("com.google.android.material:material:1.3.0")
        adapter.items = MovieData.fetchMovieData()
        adapter2.items = MovieData.fetchMovieData()

        binding.vpMain.adapter = adapter
        binding.vpMain.offscreenPageLimit = 3
        binding.vpMain.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.vpMain.setPageTransformer(getTransformer())

        binding.vpMain2.adapter = adapter2
        binding.vpMain2.clipToPadding = false
        binding.vpMain2.clipChildren = false
        binding.vpMain2.offscreenPageLimit = 3
        binding.vpMain2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.vpMain2.setPageTransformer(getTransformer())
        binding.vpMain2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, TIME)//cada 3 segundos avanzara solo
            }
        })
    }

    private fun getTransformer(): ViewPager2.PageTransformer {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(20))//margen que habra entre un card a otro
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }//encoge los cards que estan a los costados
        return compositePageTransformer
    }

    private val sliderRunnable =
        Runnable {
            binding.vpMain2.setCurrentItem(binding.vpMain2.currentItem + 1, true)
        }

    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, TIME)
    }
}