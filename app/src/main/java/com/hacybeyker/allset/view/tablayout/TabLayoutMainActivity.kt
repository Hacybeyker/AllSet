package com.hacybeyker.allset.view.tablayout

import android.app.Activity
import android.content.Intent
import com.google.android.material.tabs.TabLayoutMediator
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityTabLayoutMainBinding
import com.hacybeyker.allset.view.tablayout.adapter.TabLayoutAdapter
import com.hacybeyker.allset.view.tablayout.fragment.TabLayoutAccountFragment
import com.hacybeyker.allset.view.tablayout.fragment.TabLayoutHomeFragment
import com.hacybeyker.allset.view.tablayout.fragment.TabLayoutSettingsFragment

class TabLayoutMainActivity : BaseActivity() {

    private lateinit var binding: ActivityTabLayoutMainBinding
    private val mAdapter: TabLayoutAdapter by lazy {
        TabLayoutAdapter(
            supportFragmentManager,
            lifecycle
        )
    }

    companion object {

        private val tag = TabLayoutMainActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, TabLayoutMainActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityTabLayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        setupAdapter()
        setupTabLayout()
    }

    private fun setupAdapter() {
        mAdapter.addFragment(TabLayoutHomeFragment())
        mAdapter.addFragment(TabLayoutAccountFragment())
        mAdapter.addFragment(TabLayoutSettingsFragment())
        binding.vpFragments.adapter = mAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(
            binding.tlOptionsTop,
            binding.vpFragments,
            TabLayoutMediator.TabConfigurationStrategy() { tab, position ->
                when (position) {
                    0 -> {
                        ///tab.setIcon()
                        tab.text = "Home"
                    }
                    1 -> tab.text = "Account"
                    2 -> tab.text = "Settings"
                }
            }).attach()
    }

}