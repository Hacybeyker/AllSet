package com.hacybeyker.allset.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hacybeyker.allset.R

class BottomSheetFragment(
    private val layout: Int,
    private val themeLayout: Int? = null,
    private val cancelable: Boolean = true,
    private val canceledOnTouchOutside: Boolean = true,
    private val draggable: Boolean = true,
    private val dialogResult: (BottomSheetFragment) -> Unit,
    private val onClickListener: (BottomSheetFragment) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeLayout?.let { theme ->
            setStyle(STYLE_NORMAL, theme)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = cancelable
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside)
        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        bottomSheetBehavior.isDraggable = draggable
        view.findViewById<View>(R.id.mbBottomSheetShare).setOnClickListener { onClickListener(this) }
        dialogResult(this)
    }

}