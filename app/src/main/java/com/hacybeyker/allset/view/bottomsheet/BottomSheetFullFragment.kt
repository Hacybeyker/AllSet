package com.hacybeyker.allset.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hacybeyker.allset.R
import com.hacybeyker.allset.databinding.LayoutBottomSheetBinding

class BottomSheetFullFragment : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.layout_bottom_sheet, null)
        //binding = DataBindingUtil.bind<LayoutBottomSheetBinding>(view)
        bottomSheet.setContentView(view)
        bottomSheetBehavior=BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO)
        //view.extras
    }*/
}