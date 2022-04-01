package com.hacybeyker.allset.view.activities.animation

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityAnimationBinding
import com.hacybeyker.allset.view.activities.animation.swipe.AnimationSwipeActivityA

class AnimationActivity : BaseActivity() {

    private lateinit var binding: ActivityAnimationBinding
    private lateinit var item: Item

    companion object {

        private val tag = AnimationActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, AnimationActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityAnimationBinding.inflate(layoutInflater)
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
        binding.bAnimationSwipe.setOnClickListener {
            AnimationSwipeActivityA.newStartActivity(
                activity = this,
                data = item
            )
        }
    }

}