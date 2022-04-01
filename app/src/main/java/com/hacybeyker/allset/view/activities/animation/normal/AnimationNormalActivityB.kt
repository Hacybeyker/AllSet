package com.hacybeyker.allset.view.activities.animation.normal

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityAnimationNormalBBinding

class AnimationNormalActivityB : BaseActivity() {

    private lateinit var binding: ActivityAnimationNormalBBinding
    private lateinit var item: Item

    companion object {

        private val tag = AnimationNormalActivityB::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, AnimationNormalActivityB::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun initView() {
        binding = ActivityAnimationNormalBBinding.inflate(layoutInflater)
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
        binding.bNextA.setOnClickListener { finish() }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}