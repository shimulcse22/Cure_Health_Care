package com.devshawon.curehealthcare.base.ui

import androidx.appcompat.widget.Toolbar
import com.devshawon.curehealthcare.base.navigation.ActivityScreenSwitcher

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: Toolbar)
    fun activityScreenSwitcher(): ActivityScreenSwitcher
}