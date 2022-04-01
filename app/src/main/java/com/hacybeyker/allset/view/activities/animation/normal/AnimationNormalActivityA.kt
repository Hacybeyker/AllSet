package com.hacybeyker.allset.view.activities.animation.normal

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityAnimationNormalABinding

class AnimationNormalActivityA : BaseActivity() {

    private lateinit var binding: ActivityAnimationNormalABinding
    private lateinit var item: Item

    companion object {

        private val tag = AnimationNormalActivityA::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, AnimationNormalActivityA::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityAnimationNormalABinding.inflate(layoutInflater)
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
        binding.bNextB.setOnClickListener {
            AnimationNormalActivityB.newStartActivity(
                activity = this,
                data = item
            )
        }
    }
}