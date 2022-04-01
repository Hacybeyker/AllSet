package com.hacybeyker.allset.view.libraries

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityLibraryTouchImageViewBinding
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.webview.WebViewActivity

class LibraryTouchImageViewActivity : BaseActivity() {

    private lateinit var binding: ActivityLibraryTouchImageViewBinding
    private lateinit var item: Item

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, LibraryTouchImageViewActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityLibraryTouchImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.tvDescription.text = item.description
        setupListeners()
        setupLibrary()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun setupListeners() {
        with(binding) {
            tvDescription.setOnClickListener {
                WebViewActivity.newStartActivity(
                    activity = this@LibraryTouchImageViewActivity,
                    data = item,
                    url = "https://github.com/MikeOrtiz/TouchImageView"
                )
            }
        }
    }

    private fun setupLibrary() {
        /*----Resources----*/
        //Github:               https://github.com/MikeOrtiz/TouchImageView
        //Page:
        //References:
        //Dependencies:
        //maven(url = "https://jitpack.io")
        //implementation("com.github.MikeOrtiz:TouchImageView:3.1.0")
    }
}