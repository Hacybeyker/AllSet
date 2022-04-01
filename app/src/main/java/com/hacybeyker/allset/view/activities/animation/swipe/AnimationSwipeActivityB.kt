package com.hacybeyker.allset.view.activities.animation.swipe

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityAnimationSwipeBBinding
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface

class AnimationSwipeActivityB : BaseActivity() {

    private lateinit var binding: ActivityAnimationSwipeBBinding
    private lateinit var item: Item
    private lateinit var slide: SlidrInterface

    companion object {

        private val tag = AnimationSwipeActivityB::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, AnimationSwipeActivityB::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)

        }
    }

    override fun initView() {
        binding = ActivityAnimationSwipeBBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupListeners()
        setupActivityA()
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {
        binding.bNextA.setOnClickListener { finish() }
    }

    private fun setupActivityA() {
        slide = Slidr.attach(this)
        slide.unlock()
    }

    override fun finish() {
        super.finish()

    }
}