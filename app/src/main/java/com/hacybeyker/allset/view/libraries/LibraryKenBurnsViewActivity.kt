package com.hacybeyker.allset.view.libraries

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityLibraryKenBurnsViewBinding
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.webview.WebViewActivity

class LibraryKenBurnsViewActivity : BaseActivity() {

    private lateinit var binding: ActivityLibraryKenBurnsViewBinding
    private lateinit var item: Item
    private var state: Boolean = true

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, LibraryKenBurnsViewActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityLibraryKenBurnsViewBinding.inflate(layoutInflater)
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
                    activity = this@LibraryKenBurnsViewActivity,
                    data = item,
                    url = "https://github.com/flavioarfaria/KenBurnsView"
                )
            }

            ibControl.setOnClickListener {
                state = !state
                if (state) {
                    kbvPoster.resume()
                    ibControl.setImageResource(R.drawable.icon_pause)
                } else {
                    kbvPoster.pause()
                    ibControl.setImageResource(R.drawable.icon_play)
                }
            }
        }
    }

    private fun setupLibrary() {
        /*----Resources----*/
        //Github:           https://github.com/flavioarfaria/KenBurnsView
        //Page:
        //References:
        //Dependencies:
        //implementation("com.flaviofaria:kenburnsview:1.0.7")

    }

}