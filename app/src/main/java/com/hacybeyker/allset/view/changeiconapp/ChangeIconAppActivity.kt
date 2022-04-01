package com.hacybeyker.allset.view.changeiconapp

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityChangeIconAppBinding

class ChangeIconAppActivity : BaseActivity() {

    private lateinit var binding: ActivityChangeIconAppBinding

    companion object {

        private val tag = ChangeIconAppActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ChangeIconAppActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityChangeIconAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        binding.buttonNewIcon.setOnClickListener { changeIcon() }
        binding.buttonOldIcon.setOnClickListener { returnOld() }
    }

    private fun changeIcon() {
        //disable old icon
        val packageManager = packageManager
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "com.hacybeyker.allset.principal.MainActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        //enable new icon
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "com.hacybeyker.allset.principal.MainActivityAlias"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun returnOld() {
        //enabled old icon
        val packageManager = packageManager
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "com.hacybeyker.allset.principal.MainActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        //disable new icon
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "com.hacybeyker.allset.principal.MainActivityAlias"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

}