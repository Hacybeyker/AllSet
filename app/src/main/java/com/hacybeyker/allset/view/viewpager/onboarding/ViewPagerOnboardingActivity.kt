package com.hacybeyker.allset.view.viewpager.onboarding

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.data.MovieData
import com.hacybeyker.allset.databinding.ActivityViewPagerOnboardingBinding
import com.hacybeyker.allset.view.viewpager.onboarding.adapter.OnboardingAdapter

class ViewPagerOnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityViewPagerOnboardingBinding
    private val adapter: OnboardingAdapter by lazy { OnboardingAdapter() }
    private lateinit var item: Item

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ViewPagerOnboardingActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityViewPagerOnboardingBinding.inflate(layoutInflater)
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

        binding.mbNext.setOnClickListener {
            if (binding.vpMain.currentItem + 1 < adapter.itemCount) {
                binding.vpMain.currentItem = binding.vpMain.currentItem + 1
            } else {
                Toast.makeText(this, "Complete! start other activity", Toast.LENGTH_SHORT).show()
            }
        }

        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
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
        binding.vpMain.adapter = adapter
        binding.vpMain.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setupIndicators() {
        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        for (item in adapter.items) {
            val imageView = ImageView(applicationContext)
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.shape_indicator_onboarding_inactive
                )
            )
            imageView.adjustViewBounds = true
            imageView.setPadding(8, 0, 8, 0)
            imageView.layoutParams = layoutParams
            binding.lycDots.addView(imageView)
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.lycDots.childCount
        for (i in 0 until childCount) {
            val imageView = binding.lycDots[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.shape_indicator_onboarding_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.shape_indicator_onboarding_inactive
                    )
                )
            }
        }
        if (index == adapter.itemCount - 1) {
            binding.mbNext.text = getString(R.string.start)
        } else {
            binding.mbNext.text = getString(R.string.next)
        }
    }
}