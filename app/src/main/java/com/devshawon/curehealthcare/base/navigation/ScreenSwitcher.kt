package com.devshawon.curehealthcare.base.navigation

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)
    fun goBack()
}
