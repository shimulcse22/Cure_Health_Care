package com.devshawon.curehealthcare.ui.fragments.filter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFilterFragment(fragment: Fragment,private val companyId : ArrayList<Int> ,private val formIds : ArrayList<Int>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CompanyFilterFragment(companyId)
        1 -> FormFilterFragment(formIds)
        else -> throw IllegalAccessException("Invalid $position")
    }
}