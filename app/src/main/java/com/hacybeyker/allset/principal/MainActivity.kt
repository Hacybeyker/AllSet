package com.hacybeyker.allset.principal

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.BuildConfig
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.data.ItemData.Companion.fetchItemData
import com.hacybeyker.allset.databinding.ActivityMainBinding
import com.hacybeyker.allset.view.activities.animation.AnimationActivity
import com.hacybeyker.allset.view.activities.animation.normal.AnimationNormalActivityA
import com.hacybeyker.allset.view.activities.animation.swipe.AnimationSwipeActivityA
import com.hacybeyker.allset.view.bottomsheet.BottomSheetActivity
import com.hacybeyker.allset.view.clipboard.ClipBoardActivity
import com.hacybeyker.allset.view.extractcolor.ExtractColorActivity
import com.hacybeyker.allset.view.images.coil.ImagesCoilActivity
import com.hacybeyker.allset.view.keyboard.KeyboardActivity
import com.hacybeyker.allset.view.libraries.LibraryKenBurnsViewActivity
import com.hacybeyker.allset.view.libraries.LibraryTouchImageViewActivity
import com.hacybeyker.allset.view.libraries.scanner.LibraryScannerZxingActivity
import com.hacybeyker.allset.view.notifications.NotificationsActivity
import com.hacybeyker.allset.view.preferencessettings.PreferencesSettingsActivity
import com.hacybeyker.allset.view.recycler.RecyclerMainActivity
import com.hacybeyker.allset.view.recycler.viewtype.RecyclerViewTypeActivity
import com.hacybeyker.allset.view.screenshot.ScreenshotActivity
import com.hacybeyker.allset.view.tablayout.TabLayoutMainActivity
import com.hacybeyker.allset.view.taptargetview.TapTargetViewActivity
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.viewpager.slider.ViewPagerSliderActivity
import com.hacybeyker.allset.view.webview.WebViewActivity

class MainActivity : BaseActivity(), MainAdapter.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val mainAdapter = MainAdapter(this)
    private lateinit var item: Item

    companion object {

        private val tag = MainActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, item: Item) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(tag, item)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()

        Log.d("TAG", "Here - initView: ${this.packageName}")
        Log.d("TAG", "Here - initView: ${BuildConfig.APPLICATION_ID}")
    }

    override fun setupView() {
        binding.recyclerMain.adapter = mainAdapter
        if (::item.isInitialized) {
            activeToolbar = true
            titleToolbar = item.name
            if (item.child.isNotEmpty()) {
                mainAdapter.items = item.child.filter { it.show }
            }
        } else
            mainAdapter.items = fetchItemData().filter { it.show }
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            dataTemp?.let {
                this.item = dataTemp as Item
            }
        }
    }

    override fun onItemSelected(item: Item) {
        if (item.child.isNotEmpty()) {
            newStartActivity(activity = this, item = item)
        } else {
            when (item.classActivity) {
                KeyboardActivity::class.java.simpleName -> {
                    KeyboardActivity.newStartActivity(activity = this, data = item)
                }
                BottomSheetActivity::class.java.simpleName -> {
                    BottomSheetActivity.newStartActivity(activity = this, data = item)
                }
                ScreenshotActivity::class.java.simpleName -> {
                    ScreenshotActivity.newStartActivity(activity = this, data = item)
                }
                AnimationActivity::class.java.simpleName -> {
                    AnimationActivity.newStartActivity(activity = this, data = item)
                }
                AnimationSwipeActivityA::class.java.simpleName -> {
                    AnimationSwipeActivityA.newStartActivity(activity = this, data = item)
                }
                AnimationNormalActivityA::class.java.simpleName -> {
                    AnimationNormalActivityA.newStartActivity(activity = this, data = item)
                }
                RecyclerMainActivity::class.java.simpleName -> {
                    RecyclerMainActivity.newStartActivity(activity = this, data = item)
                }
                ViewPagerOnboardingActivity::class.java.simpleName -> {
                    ViewPagerOnboardingActivity.newStartActivity(activity = this, data = item)
                }
                ViewPagerSliderActivity::class.java.simpleName -> {
                    ViewPagerSliderActivity.newStartActivity(activity = this, data = item)
                }
                ImagesCoilActivity::class.java.simpleName -> {
                    ImagesCoilActivity.newStartActivity(activity = this, data = item)
                }
                WebViewActivity::class.java.simpleName -> {
                    WebViewActivity.newStartActivity(activity = this, data = item)
                }
                LibraryKenBurnsViewActivity::class.java.simpleName -> {
                    LibraryKenBurnsViewActivity.newStartActivity(activity = this, data = item)
                }
                LibraryTouchImageViewActivity::class.java.simpleName -> {
                    LibraryTouchImageViewActivity.newStartActivity(activity = this, data = item)
                }
                LibraryScannerZxingActivity::class.java.simpleName -> {
                    LibraryScannerZxingActivity.newStartActivity(activity = this, data = item)
                }
                NotificationsActivity::class.java.simpleName -> {
                    NotificationsActivity.newStartActivity(activity = this, data = item)
                }
                PreferencesSettingsActivity::class.java.simpleName -> {
                    PreferencesSettingsActivity.newStartActivity(activity = this, data = item)
                }
                TabLayoutMainActivity::class.java.simpleName -> {
                    TabLayoutMainActivity.newStartActivity(activity = this, data = item)
                }
                RecyclerViewTypeActivity::class.java.simpleName -> {
                    RecyclerViewTypeActivity.newStartActivity(activity = this, data = item)
                }
                ClipBoardActivity::class.java.simpleName -> {
                    ClipBoardActivity.newStartActivity(activity = this, data = item)
                }
                ExtractColorActivity::class.java.simpleName -> {
                    ExtractColorActivity.newStartActivity(activity = this, data = item)
                }
                TapTargetViewActivity::class.java.simpleName -> {
                    TapTargetViewActivity.newStartActivity(activity = this, data = item)
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.message_not_find_activity),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}