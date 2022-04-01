package com.hacybeyker.allset.view.extractcolor

/**
 * Created by Carlos Osorio on 29/07/2021
 */

data class ColorVO(
    val type: String,
    val colorHex: String? = "",
    val description: String? = "",
    val rgb: Int? = null
)