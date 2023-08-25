package com.hacybeyker.allset.view.keyboard.component

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputLayout
import com.hacybeyker.allset.R
import com.hacybeyker.allset.databinding.ComponentInputTextBinding


class InputText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ComponentInputTextBinding by lazy {
        ComponentInputTextBinding.inflate(LayoutInflater.from(context), this, true)
    }

    val textInputLayoutFocusedRectEnabled: Boolean = true
    val parentRect: Rect by lazy { Rect() }

    private var text = ""
        set(value) {
            field = value
            setAttributeValue(value)
        }

    private fun setAttributeValue(value: String) {
        binding.tilInfo.error = "helouda aes un error"
        binding.tietInfo.setText(value)
    }

    init {
        ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
        loadAttributes(context, attrs)
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val attributeSet =
                context.obtainStyledAttributes(it, com.hacybeyker.allset.R.styleable.InputText)
            attributeSet.let { typedArray ->
                text = typedArray.getString(com.hacybeyker.allset.R.styleable.InputText_text) ?: ""
            }

            attributeSet.recycle()
        }
    }

    private fun getTextInputLayout(): TextInputLayout? {
        var parent = parent
        while (parent is View) {
            if (parent is TextInputLayout) {
                return parent
            }
            parent = parent.getParent()
        }
        return null
    }



    override fun getFocusedRect(@Nullable r: Rect?) {
        super.getFocusedRect(r)
        val textInputLayout = getTextInputLayout()
        if (textInputLayout != null && textInputLayoutFocusedRectEnabled
            && r != null
        ) {
            textInputLayout.getFocusedRect(parentRect)
            r.bottom = parentRect.bottom
        }
    }

    override fun getGlobalVisibleRect(@Nullable r: Rect?, @Nullable globalOffset: Point?): Boolean {
        val result = super.getGlobalVisibleRect(r, globalOffset)
        val textInputLayout = getTextInputLayout()
        if (textInputLayout != null && textInputLayoutFocusedRectEnabled
            && r != null
        ) {
            textInputLayout.getGlobalVisibleRect(parentRect, globalOffset)
            r.bottom = parentRect.bottom
        }
        return result
    }

    override fun requestRectangleOnScreen(rectangle: Rect?): Boolean {
        val result = super.requestRectangleOnScreen(rectangle)
        val textInputLayout = getTextInputLayout()
        if (textInputLayout != null && textInputLayoutFocusedRectEnabled) {
            parentRect.set(
                0, textInputLayout.height
                        - resources.getDimensionPixelOffset(R.dimen.dimen_31dp),
                textInputLayout.width,
                textInputLayout.height
            )
            textInputLayout.requestRectangleOnScreen(parentRect, true)
        }
        return result
    }

}