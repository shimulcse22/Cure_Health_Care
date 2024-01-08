package com.devshawon.curehealthcare.ui.fragments.filter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFilterFragment(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CompanyFilterFragment()
        1 -> FormFilterFragment()
        else -> throw IllegalAccessException("Invalid $position")
    }
}