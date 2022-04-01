package com.hacybeyker.allset.view.preferencessettings

import android.app.Activity
import android.content.Intent
import androidx.preference.PreferenceManager
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityPreferencesSettingsBinding

class PreferencesSettingsActivity : BaseActivity() {

    private lateinit var binding: ActivityPreferencesSettingsBinding
    private lateinit var item: Item

    companion object {

        private val tag = PreferencesSettingsActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, PreferencesSettingsActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityPreferencesSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.tvDescription.text = item.description
        setupListeners()
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
            mvSettingsPreferences.setOnClickListener {
                openPreferences()
            }
        }
    }

    private fun openPreferences() {
        SettingsActivity.newStartActivity(this, item)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val signature = sharedPreferences.getString("signature", "")
        val switch = sharedPreferences.getBoolean("sync", false)
        val reply = sharedPreferences.getString("reply", "")
        val check = sharedPreferences.getBoolean("checkbox", false)
        with(binding) {
            tvTitleSignatureContent.text = signature
            tvTitleReplyContent.text = switch.toString()
            tvTitleImagesContent.text = check.toString()
            tvTitleSyncContent.text = reply.toString()
        }
    }
}