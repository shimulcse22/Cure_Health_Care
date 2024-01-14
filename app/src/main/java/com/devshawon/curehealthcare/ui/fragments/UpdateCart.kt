package com.devshawon.curehealthcare.ui.fragments

import com.devshawon.curehealthcare.models.ProductData

interface UpdateCart {
    fun inCreaseItem(data : ProductData)
    fun decreaseItem(data : ProductData,position : Int)
}