package com.hacybeyker.allset.view.libraries.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.BuildConfig
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityLibraryScannerZxingBinding
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.webview.WebViewActivity
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream

class LibraryScannerZxingActivity : BaseActivity() {

    private lateinit var binding: ActivityLibraryScannerZxingBinding
    private var item: Item? = null

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName
        private const val FOLDER_MAIN = "helouda"
        private const val TYPE = "image/*"
        private const val EXTENSION_PNG = "png"

        fun newStartActivity(activity: Activity, data: Item?) {
            val intent = Intent(activity, LibraryScannerZxingActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityLibraryScannerZxingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item?.name ?: ""
        binding.tvDescription.text = item?.description ?: ""
        setupListeners()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun setupListeners() {
        with(binding) {
            tvDescription.setOnClickListener {
                startWebView()
            }

            mbScan.setOnClickListener {
                setupLibrary()
            }

            mbGenerate.setOnClickListener {
                generateQR()
            }

            mbGenerateShared.setOnClickListener {
                share()
            }
        }
    }

    private fun startWebView() {
        WebViewActivity.newStartActivity(
            activity = this@LibraryScannerZxingActivity,
            data = item,
            url = "https://github.com/journeyapps/zxing-android-embedded"
        )
    }

    private fun setupLibrary() {
        /*----Resources----*/
        //Github:               https://github.com/journeyapps/zxing-android-embedded
        //Page:
        //References:
        //Dependencies:
        //android:hardwareAccelerated="true"
        //<uses-sdk tools:overrideLibrary="com.google.zxing.client.android" />
        //implementation("com.journeyapps:zxing-android-embedded:4.2.0") { isTransitive = false }
        //implementation("com.google.zxing:core:3.4.0")
        //Resources:
        //https://stackoverflow.com/questions/45893294/permission-denial-with-file-provider-through-intent
        //https://stackoverflow.com/questions/57689792/permission-denial-while-sharing-file-with-fileprovider


        /*---------- Info ---------- */
        //setPrompt:                texto a mostrar
        //setCameraId:              usar la camara especifica del dispositivo
        //setBeepEnabled            habilitar el sonido beep
        //setTorchEnabled           habilitar el flash
        //setBarcodeImageEnabled    guardar la imagen escaneada y enviarla por el intent
        //setDesiredBarcodeFormats  se puede cambiar el tipo

        val integrator = IntentIntegrator(this).apply {
            setPrompt("Scan a barcode")
            setCameraId(0)
            setBeepEnabled(false)
            setTorchEnabled(false)
            setBarcodeImageEnabled(true)
        }
        integrator.initiateScan()
    }

    private fun generateQR() {
        val data = binding.edGenerateText.text.toString()
        if (data.isNotEmpty()) {
            val bitmap = generateBitmapQR(data)
            binding.ivGenerateImage.setImageBitmap(bitmap)
            binding.mbGenerateShared.visibility = View.VISIBLE
        } else {
            Toast.makeText(applicationContext, "Input vacio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateBitmapQR(data: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 550, 550)
    }

    private fun share() {
        val uri = convertBitmapToFile(applicationContext, convertDrawableToBitmap())
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = TYPE
        }

        val chooser = Intent.createChooser(shareIntent, resources.getText(R.string.send_qr))
        val resInfoList =
            this.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        startActivity(chooser)
    }

    private fun getDrawableFromImageView(): Drawable {
        return binding.ivGenerateImage.drawable
    }

    private fun convertDrawableToBitmap(): Bitmap {
        return (getDrawableFromImageView() as BitmapDrawable).bitmap
    }

    private fun convertBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val folder = File(externalCacheDir, FOLDER_MAIN)
        if (!folder.exists() && !folder.mkdir()) {
            folder.mkdirs()
        }
        val file = File(folder, "${generateTimeMillis()}.$EXTENSION_PNG")
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

    private fun showResult(result: IntentResult) {
        val imagePath = result.barcodeImagePath
        val fileImage = File(imagePath)
        if (fileImage.exists()) {
            val bitmap = BitmapFactory.decodeFile(fileImage.absolutePath)
            binding.ivScannerImage.setImageBitmap(bitmap)
            binding.tvScannerText.text = result.contents
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, getString(R.string.scan_cancel), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.scan_success), Toast.LENGTH_LONG).show()
                showResult(result)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}