package com.hacybeyker.allset

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun initView()
    abstract fun setupView()
    open var activeToolbar: Boolean = false
    open var titleToolbar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setupView()
        setupActionBar()
    }

    private fun setupActionBar() {
        if (activeToolbar) {
            title = if (titleToolbar.isNotEmpty()) titleToolbar else this::class.java.simpleName
            val actionBar = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}