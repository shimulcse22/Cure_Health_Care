package com.devshawon.curehealthcare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.devshawon.curehealthcare.R

class CureHealthCareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure_health_care)
    }

    fun getStackCount(): Int {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment?
        return navHostFragment!!.childFragmentManager.backStackEntryCount
    }
}