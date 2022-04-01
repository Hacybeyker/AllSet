package com.hacybeyker.allset.view.webview

import android.app.Activity
import android.content.Intent
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityWebViewBinding

class WebViewActivity : BaseActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var item: Item
    private lateinit var url: String

    companion object {

        private val tag = WebViewActivity::class.java.simpleName
        private val urlParams = "url"


        fun newStartActivity(activity: Activity, data: Item?) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }

        fun newStartActivity(activity: Activity, data: Item?, url: String) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(tag, data)
            intent.putExtra(urlParams, url)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.tvDescription.text = item.description
        loadWebView()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            val urlTemp = intent.getStringExtra(urlParams)
            if (dataTemp != null)
                item = dataTemp
            if (urlTemp != null)
                url = urlTemp
        }
    }

    private fun loadWebView() {
        /*----Resources----*/
        //Github:
        //Page:         https://developer.android.com/guide/webapps/webview?hl=es-419
        //Dependency:

        binding.wvMain.settings.javaScriptEnabled = true
        binding.wvMain.settings.defaultTextEncodingName = "utf-8"
        binding.wvMain.settings.useWideViewPort = true
        binding.wvMain.settings.loadWithOverviewMode = true
        if (::url.isInitialized) {
            binding.wvMain.loadUrl(url)
        } else {
            binding.wvMain.loadUrl("https:www.google.com")
        }
    }
}