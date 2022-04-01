package com.hacybeyker.allset.view.images.coil

import android.app.Activity
import android.content.Intent
import coil.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.data.imageServer1
import com.hacybeyker.allset.databinding.ActivityImagesCoilBinding
import com.hacybeyker.allset.view.webview.WebViewActivity

class ImagesCoilActivity : BaseActivity() {

    private lateinit var binding: ActivityImagesCoilBinding
    private lateinit var item: Item

    companion object {

        private val tag = ImagesCoilActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, ImagesCoilActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityImagesCoilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.descriptionTextView.text = item.description
        loadImagesWithCoil()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun loadImagesWithCoil() {
        /*----Resources----*/
        //Github:           https://github.com/coil-kt/coil
        //Page:             https://coil-kt.github.io/coil/getting_started/
        //References:
        //Dependencies:     implementation("io.coil-kt:coil:1.2.2")

        //Con el metodo load y agregando la url de la imagen ya sea local, remota o de tipo file, se podria visualizar la imagen
        //Sin embargo se puede agregar el crossfade que permite mostrar la imagen con una animacion y no de golpe, ademas de agregarle una duracion para que se muestre en su totalidad
        //Ademas con el metodo transformations:
        // RoundedCornersTransformation: se puede transformar los bordes definiendo el radio
        // CircleCropTransformation: se puede transformar a un circulo
        // BlurTransformation: se puede transformar con un ofuscado
        // GrayscaleTransformation: se puede transformar a escala a grises

        //Regular
        binding.ivRegularPoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
        }

        //Rounded Corner
        binding.ivRoundedPoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(15f))
        }

        //Circular Corner
        binding.ivCirclePoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
            transformations(CircleCropTransformation())
        }

        //Gray Scale
        binding.ivGrayScalePoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
            transformations(GrayscaleTransformation())
        }

        //Blur
        binding.ivBlurPoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
            transformations(BlurTransformation(applicationContext, 15f))
        }

        //Multiple transformation
        binding.ivMultipleTransformationPoster.load(imageServer1) {
            crossfade(true)
            crossfade(500)
            transformations(
                CircleCropTransformation(),
                BlurTransformation(applicationContext, 1f),
                GrayscaleTransformation()
            )
        }

        binding.descriptionTextView.setOnClickListener {
            WebViewActivity.newStartActivity(this, item, "https://github.com/coil-kt/coil")
        }
    }
}