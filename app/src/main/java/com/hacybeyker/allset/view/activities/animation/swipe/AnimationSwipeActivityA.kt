package com.hacybeyker.allset.view.activities.animation.swipe

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityAnimationSwipeABinding
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity

class AnimationSwipeActivityA : BaseActivity() {

    private lateinit var binding: ActivityAnimationSwipeABinding
    private lateinit var item: Item

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, AnimationSwipeActivityA::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityAnimationSwipeABinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupListeners()
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {
        binding.bNextB.setOnClickListener { AnimationSwipeActivityB.newStartActivity(activity = this, data = item) }
    }
}