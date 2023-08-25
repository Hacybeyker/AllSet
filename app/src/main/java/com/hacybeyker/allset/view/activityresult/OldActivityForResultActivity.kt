package com.hacybeyker.allset.view.activityresult

import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityOldForResultBinding

class OldActivityForResultActivity : BaseActivity() {

    private lateinit var binding: ActivityOldForResultBinding
    private lateinit var item: Item

    companion object {
        private val TAG = ActivityOldForResultBinding::class.java.simpleName
    }

    override fun initView() {
        binding = ActivityOldForResultBinding.inflate(layoutInflater)
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
            val dataTemp = intent.getParcelableExtra<Item>(TAG)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun setupListeners() {

    }
}