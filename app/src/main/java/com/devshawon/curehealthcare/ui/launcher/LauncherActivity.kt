package com.devshawon.curehealthcare.ui.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.auth.AuthActivity
import com.devshawon.curehealthcare.util.SharedPreferenceStorage

class LauncherActivity : Activity() {
    lateinit var preferences : SharedPreferenceStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher_activity)
        preferences = SharedPreferenceStorage(applicationContext)
        if(preferences.isLogin){
            val intent  = Intent(this,CureHealthCareActivity :: class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this,AuthActivity :: class.java)
            startActivity(intent)
            finish()
        }
    }
}