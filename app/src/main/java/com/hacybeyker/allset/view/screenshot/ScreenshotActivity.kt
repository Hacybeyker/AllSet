package com.hacybeyker.allset.view.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.BuildConfig
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityScreenshotBinding
import java.io.File
import java.io.FileOutputStream

class ScreenshotActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityScreenshotBinding
    private lateinit var item: Item
    private lateinit var mMap: GoogleMap

    companion object {

        private val tag = ScreenshotActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ScreenshotActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityScreenshotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.lGeneric.tvDescription.text = item.description
        setupMap()
        //setupFragmentContainerView()
        setupListeners()
    }


    private fun captureScreenshotMap() {
        val snapShotCallback = mMap.snapshot {
            it?.let {
                //convertBitmapToFile(this, it)

                val view = window.decorView.findViewById<ConstraintLayout>(R.id.clContainer)
                view.setDrawingCacheEnabled(true)
                val bitmap = view.getDrawingCache()
                val overlay = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
                val canvas = Canvas(overlay)
                canvas.drawBitmap(it, Matrix(), null)
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)

                val bgDrawable = view.background
                if (bgDrawable != null)
                    bgDrawable.draw(canvas)
                else
                    canvas.drawColor(Color.WHITE)
                view.draw(canvas)
                convertBitmapToFile(this, overlay)
            }
        }
    }


    private fun setupFragmentContainerView() {
        supportFragmentManager.beginTransaction().replace(R.id.fContainerView, ScreenshotFragment())
            .commit()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getIntentData() {
        intent?.let {
            val dataTemp = it.getParcelableExtra<Item>(tag)
            this.item = dataTemp as Item
        }
    }

    private fun setupListeners() {
        binding.bScreenshot.setOnClickListener {
            //captureScreenshotMap()
            takeScreenshot()
        }

        binding.bScreenshot2.setOnClickListener {
            val view = window.decorView.rootView
            val bitmap = Bitmap.createBitmap(view.drawToBitmap())
            convertBitmapToFile(this, bitmap)
        }
    }

    private fun takePhoto2() {

    }

    private fun takeScreenshot() {
        val viewGlobal = binding.nsvContainer
        val view =
            this.findViewById<View>(android.R.id.content) as ViewGroup//binding.clContainer//window.decorView.rootView
        val vg = view.getChildAt(0) as ViewGroup
        if (vg.id == -1)
            vg.id = View.generateViewId()
        vg.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

        vg.isDrawingCacheEnabled = true
        /*view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)*/
        Log.d(
            "TAG",
            "Here - setupListeners: measure: h-${vg.measuredHeight} : w-${vg.measuredWidth}"
        )
        Log.d("TAG", "Here - setupListeners: view: h-${vg.height} : w-${vg.width}")
        Log.d(
            "TAG",
            "Here - setupListeners: view: h-${viewGlobal.getChildAt(0).height} : w-${
                viewGlobal.getChildAt(
                    0
                ).width
            }"
        )

        val bitmap = Bitmap.createBitmap(
            vg.width,
            vg.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        val bgDrawable = vg.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        vg.draw(canvas)
        vg.isDrawingCacheEnabled = false
        convertBitmapToFile(this, bitmap)
    }

    private fun convertBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val folder = File(externalCacheDir, "demoxxx")
        if (!folder.exists() && !folder.mkdir()) {
            folder.mkdirs()
        }
        val file = File(folder, "${generateTimeMillis()}.png")
        file.createNewFile()
        val bytes = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        bytes.flush()
        bytes.close()
        //bitmap.recycle()
        return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
    }

    private fun generateTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(10f)
        mMap.setMaxZoomPreference(15f)
        //mMap.snapshot(this)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val home = LatLng(-12.077246, -77.036668)
        mMap.addMarker(MarkerOptions().position(home).title("My Home").draggable(true))

        val cameraHomePosition = CameraPosition.Builder()
            .target(home)
            .zoom(15f)              //1-5-10-15-20
            .bearing(0f)            //rotacion - 0-365
            .tilt(90f)              //inclinacion, da efecto 3D - 90
            .build()

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(home))
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraHomePosition))
    }

    /* override fun onSnapshotReady(p0: Bitmap?) {
         TODO("Not yet implemented")
     }*/
}