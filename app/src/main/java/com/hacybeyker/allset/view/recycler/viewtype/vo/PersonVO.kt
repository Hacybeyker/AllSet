package com.hacybeyker.allset.view.recycler.viewtype.vo

/**
 * Created by Carlos Osorio on 19/07/2021
 */

data class PersonVO(
    val name: String,
    val age: Int,
    val type: TypePersonVO,
    val hobbies: ArrayList<HobieVO>? = null
)

data class HobieVO(
    val name: String,
    val description: String
)

enum class TypePersonVO {
    NORMAL,
    HARD
}