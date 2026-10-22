package com.hacybeyker.allset.feature.catalog

import androidx.navigation3.runtime.NavKey

data class Demo(
    val id: String,
    val name: String,
    val description: String,
    val show: Boolean = true,
    val children: List<Demo> = emptyList(),
    val destination: NavKey? = null
)
