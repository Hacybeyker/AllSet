package com.hacybeyker.allset.view.tablayout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by Carlos Osorio on 13/07/2021
 */

class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val listFragment = arrayListOf<Fragment>()

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
        /*return when (position) {
            0 -> TabLayoutHomeFragment()
            1 -> TabLayoutAccountFragment()
            2 -> TabLayoutSettingsFragment()
            else -> TabLayoutHomeFragment()
        }*/
    }

    fun addFragment(fragment: Fragment) {
        listFragment.add(fragment)
    }
}