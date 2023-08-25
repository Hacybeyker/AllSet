package com.hacybeyker.allset.view.activityresult

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityResultBinding

class ActivityResultActivity : BaseActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var item: Item

    private val newActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RES ULT_OK) {
                Toast.makeText(this, "Message OK :)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Message NOK", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        private val TAG = ActivityResultActivity::class.java.simpleName
    }

    override fun initView() {
        binding = ActivityResultBinding.inflate(layoutInflater)
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
        binding.bOldActivityForResult.setOnClickListener {

        }

        binding.bNewActivityForResult.setOnClickListener {
            val intent = Intent(this, NewActivityForResultActivity::class.java)
            newActivityResultLauncher.launch(intent )
        }
    }
}