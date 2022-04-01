package com.hacybeyker.allset.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Carlos Osorio on 21/05/2021
 */

@Parcelize
data class Item(
    var name: String,
    var show: Boolean = true,
    var description: String = "",
    var classActivity: String,
    var child: ArrayList<Item> = arrayListOf()
) : Parcelable