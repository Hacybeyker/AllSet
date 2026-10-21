package com.hacybeyker.allset.feature.catalog

data class Demo(
    val id: String,
    val name: String,
    val description: String,
    val show: Boolean = true,
    val children: List<Demo> = emptyList()
)
