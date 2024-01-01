package com.devshawon.curehealthcare.ui.fragments

import com.devshawon.curehealthcare.models.ProductData

interface OnItemClick {
    fun onPlusIconClick(item : ProductData)
    fun onMinusIconClick(item : ProductData)
}