package com.hacybeyker.allset.view.extractcolor

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityExtractColorBinding

class ExtractColorActivity : BaseActivity(), ColorAdapter.OnItemSelectedListener {

    private lateinit var binding: ActivityExtractColorBinding
    private val adapter: ColorAdapter by lazy { ColorAdapter(this) }

    companion object {

        private val tag = ExtractColorActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ExtractColorActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityExtractColorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        Log.d("TAG", "Here - setupView: Helouda!")
        //https://developer.android.com/training/material/palette-colors
        //https://guides.codepath.com/android/Dynamic-Color-using-Palettes
        //https://developer.android.com/reference/kotlin/androidx/core/graphics/ColorUtils
        // implementation "androidx.palette:palette-ktx:1.0.0"
        //val darkVibrantSwatch: Palette.Swatch
        //lightVibrantSwatch
        //darkVibrantSwatch
        //mutedSwatch
        //lightMutedSwatch
        //darkMutedSwatch

        binding.rvColors.setHasFixedSize(true)
        binding.rvColors.adapter = adapter


        val bitmapDrawable = binding.ivScene.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        Palette.from(bitmap).generate {
            it?.let { palette ->
                val dominantColor = palette.getDominantColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )

                val darkMutedSwatch = palette.darkMutedSwatch
                val darkVibrantSwatch = palette.darkVibrantSwatch
                val dominantSwatch = palette.dominantSwatch
                val lightMutedSwatch = palette.lightMutedSwatch
                val lightVibrantSwatch = palette.lightVibrantSwatch
                val mutedSwatch = palette.mutedSwatch
                val vibrantSwatch = palette.vibrantSwatch

                /*Log.d(
                    "TAG",
                    "Here - setupView colorToRgb: ${ConvertColor.colorToRgb(dominantColor)}"
                )
                Log.d(
                    "TAG",
                    "Here - setupView hslToRgb: ${ConvertColor.hslToRgb(dominantSwatch?.hsl!!)}"
                )*/


                Log.d("TAG", "Here - setupView darkMutedSwatch: $darkMutedSwatch")
                Log.d("TAG", "Here - setupView dominantColor: $dominantColor")


                val colors = arrayListOf(
                    ColorVO(
                        type = "darkMutedSwatch",
                        colorHex = ConvertColor.hslToRgbString(darkMutedSwatch?.hsl),
                        description = ConvertColor.paletteSwatchToString(darkMutedSwatch),
                        rgb = darkMutedSwatch?.rgb
                    ),
                    ColorVO(
                        type = "darkVibrantSwatch",
                        colorHex = ConvertColor.hslToRgbString(darkVibrantSwatch?.hsl),
                        description = darkVibrantSwatch?.hsl.toString(),
                        rgb = darkVibrantSwatch?.rgb
                    ),
                    ColorVO(
                        type = "dominantSwatch",
                        colorHex = ConvertColor.hslToRgbString(dominantSwatch?.hsl),
                        description = dominantSwatch?.hsl.toString(),
                        rgb = dominantSwatch?.rgb
                    ),
                    ColorVO(
                        type = "lightMutedSwatch",
                        colorHex = ConvertColor.hslToRgbString(lightMutedSwatch?.hsl),
                        description = lightMutedSwatch?.hsl.toString(),
                        rgb = lightMutedSwatch?.rgb
                    ),
                    ColorVO(
                        type = "lightVibrantSwatch",
                        colorHex = ConvertColor.hslToRgbString(lightVibrantSwatch?.hsl),
                        description = lightVibrantSwatch?.hsl.toString(),
                        rgb = lightVibrantSwatch?.rgb
                    ),
                    ColorVO(
                        type = "mutedSwatch",
                        colorHex = ConvertColor.hslToRgbString(mutedSwatch?.hsl),
                        description = mutedSwatch?.hsl.toString(),
                        rgb = mutedSwatch?.rgb
                    ),
                    ColorVO(
                        type = "vibrantSwatch",
                        colorHex = ConvertColor.hslToRgbString(vibrantSwatch?.hsl),
                        description = vibrantSwatch?.hsl.toString(),
                        rgb = vibrantSwatch?.rgb
                    )
                )
                setupChangeData(colors)
            }
        }
    }

    private fun setupChangeData(colors: List<ColorVO>) {
        adapter.items = colors
    }

    override fun onItemSelected(item: ColorVO) {
        item.rgb?.let { color ->
            binding.clRoot.setBackgroundColor(color)
            window?.statusBarColor = color
            Log.d("TAG", "Here - onItemSelected: $color")
            /*color?.let {
                val window: Window = window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = it
            }*/

        }

        //binding.clRoot.setBackgroundColor(item.color!!.rgb)
    }
}