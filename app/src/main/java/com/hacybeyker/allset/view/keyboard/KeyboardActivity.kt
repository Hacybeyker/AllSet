package com.hacybeyker.allset.view.keyboard

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityKeyboardBinding

class KeyboardActivity : BaseActivity() {

    private lateinit var binding: ActivityKeyboardBinding
    private lateinit var item: Item
    /* private lateinit var imm: InputMethodManager*/

    companion object {

        private val tag = KeyboardActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, KeyboardActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityKeyboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager*/
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupListeners()

        //ViewCompat.setAccessibilityLiveRegion(binding.tilInfo, ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE)

        //binding.tilInfo.requestFocus()

        //imm.showSoftInput(binding.vInfo, InputMethodManager.SHOW_IMPLICIT)
        /*Log.d("TAG", "Here - setupView: ${imm.isActive}")*/

        /*binding.clContainerKeyboard.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.clContainerKeyboard.getWindowVisibleDisplayFrame(rec)

            //finding screen height
            val screenHeight = binding.clContainerKeyboard.rootView.height

            //finding keyboard height
            val keypadHeight = screenHeight - rec.bottom

            if (keypadHeight > screenHeight * 0.15) {
                Toast.makeText(this@KeyboardActivity, "VISIBLE KEYBOARD", Toast.LENGTH_LONG).show()
                binding.nsvContainer.smoothScrollTo(0, binding.nsvContainer.bottom)

            } else {
                Toast.makeText(this@KeyboardActivity, "NO KEYBOARD", Toast.LENGTH_LONG).show()
            }
        }*/

        val window = this.window
        WindowCompat.setDecorFitsSystemWindows(window, false)


        ViewCompat.setOnApplyWindowInsetsListener(binding.clContainerKeyboard, callBack)


    }

    val callBack = OnApplyWindowInsetsListener { view, insets ->
        val imeHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
        Log.e("tag", "Here - onKeyboardOpenOrClose imeHeight = $imeHeight")
        // todo: logic
        val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        if (isKeyboardVisible) {
            // do something
        } else {
            // do something else
        }
        insets ?: WindowInsetsCompat(null)
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {

    }
}