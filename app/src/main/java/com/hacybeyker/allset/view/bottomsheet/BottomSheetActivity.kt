package com.hacybeyker.allset.view.bottomsheet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityBottomSheetBinding


class BottomSheetActivity : BaseActivity() {

    private lateinit var binding: ActivityBottomSheetBinding
    private lateinit var item: Item

    private val ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 1000

    private var cancelable: Boolean = true
    private var canceledOnTouchOutside: Boolean = true
    private var draggable: Boolean = true

    companion object {

        private val tag = BottomSheetActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, BottomSheetActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()

        ///TODO verificar si un permiso esta permitido o denegado
        Log.d(
            "TAG",
            "Here - initView1: ${shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)}"
        )

        Log.d(
            "TAG",
            "Here - initView2: ${checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)}"
        )

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Here - Permission granted successfully");
                Toast.makeText(this, "Here - Permission granted successfully", Toast.LENGTH_LONG)
                    .show();
                ///TODO verificar si un permiso esta permitido o denegado
                Log.d(
                    "TAG",
                    "Here - onRequestPermissionsResult1: ${
                        shouldShowRequestPermissionRationale(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }"
                )

                Log.d(
                    "TAG",
                    "Here - onRequestPermissionsResult2: ${checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)}"
                )
            } else {
                ///TODO verificar si un permiso esta permitido o denegado
                Log.d(
                    "TAG",
                    "Here - onRequestPermissionsResult else1: ${
                        shouldShowRequestPermissionRationale(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }"
                )

                Log.d(
                    "TAG",
                    "Here - onRequestPermissionsResult else2: ${
                        checkCallingOrSelfPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }"
                )
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupListeners()
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {
        binding.apply {
            mbBottomSheetDialog.setOnClickListener { showBSDialog() }
            mbBottomSheetDialogFragment.setOnClickListener { showBSDialogFragment() }
            /*bExpanded.setOnClickListener { showBSBehaviorExpanded() }
            bCollapsed.setOnClickListener { showBSBehaviorCollapsed() }*/
        }
    }

    private fun showBSDialog() {
        getCheckValues()
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(this).inflate(
            R.layout.layout_bottom_sheet,
            findViewById<ConstraintLayout>(R.id.clBottomSheet),
            false
        )
        bottomSheetView.findViewById<View>(R.id.mbBottomSheetShare).setOnClickListener {
            Toast.makeText(this, "Share...", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setCancelable(cancelable)
        bottomSheetDialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.behavior.isDraggable = draggable
        bottomSheetDialog.show()
    }

    private fun showBSDialogFragment() {
        getCheckValues()
        BottomSheetFragment(
            layout = R.layout.layout_bottom_sheet,
            cancelable = cancelable,
            canceledOnTouchOutside = canceledOnTouchOutside,
            draggable = draggable
        ) {

        }.show(supportFragmentManager, BottomSheetFragment::class.java.name)
    }

    /*private fun showBSBehaviorExpanded() {
        val bottomSheetView = LayoutInflater.from(this).inflate(
            R.layout.layout_bottom_sheet,
            findViewById<ConstraintLayout>(R.id.clBottomSheet),
            false
        )
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showBSBehaviorCollapsed() {
        val bottomSheetBehavior: BottomSheetBehavior<*>?
        val bottomSheet: View = findViewById(R.id.clBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }*/

    private fun getCheckValues() {
        cancelable = binding.cbCancelable.isChecked
        canceledOnTouchOutside = binding.cbCancelOnTouchOutside.isChecked
        draggable = binding.cbDraggable.isChecked
    }


}

/*
* Agregar esto en Themas
*     <!-- Inicio - Se usa en BottomSheetActivity  -->
    <style name="BottomSheetDialogTheme" parent="Theme.Design.Light.BottomSheetDialog">
        <item name="bottomSheetStyle">@style/BottomSheetStyle</item>
    </style>

    <style name="BottomSheetStyle" parent="Widget.Design.BottomSheet.Modal">
        <item name="android:background">@android:color/transparent</item>
    </style>
    <!-- Fin - Se usa en BottomSheetActivity  -->
* */