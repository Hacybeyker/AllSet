package com.hacybeyker.allset.view.taptargetview

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.widget.Toast
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityTapTargetViewBinding

class TapTargetViewActivity : BaseActivity() {

    private lateinit var binding: ActivityTapTargetViewBinding
    private lateinit var item: Item

    companion object {

        private val tag = TapTargetViewActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, TapTargetViewActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityTapTargetViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupListeners()
        setupTapTargetVieW()
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {
        binding.bPayment.setOnClickListener {
            Toast.makeText(this, "Click", Toast.LENGTH_LONG).show()
        }
    }

    // https://github.com/KeepSafe/TapTargetView

    private fun setupTapTargetVieW() {
        TapTargetView.showFor(
            this, TapTarget.forView(binding.bPayment, "This is a button", "Click it if you want!")
                .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                .textColor(R.color.white)            // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(30), TapTargetView.Listener().apply {
                Toast.makeText(this@TapTargetViewActivity, "Click Inside", Toast.LENGTH_LONG).show()
            }
        )
    }
}