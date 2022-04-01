package com.hacybeyker.allset.view.clipboard

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityClipBoardBinding

class ClipBoardActivity : BaseActivity() {

    private lateinit var binding: ActivityClipBoardBinding

    companion object {

        private val tag = ClipBoardActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ClipBoardActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityClipBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        setuptListener()
    }

    private fun setuptListener() {
        binding.ibCopy.setOnClickListener {
            copyTextToClipBoard()
        }
    }

    private fun copyTextToClipBoard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("desription", binding.etCopy.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copiado y pegado a la vez", Toast.LENGTH_SHORT).show()
        binding.tvPaste.text = clipboard.primaryClip?.getItemAt(0)?.text.toString()
    }
}