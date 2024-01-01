package com.devshawon.curehealthcare.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.devshawon.curehealthcare.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    this.context?.showDialog(cancelable, cancelableTouchOutside, builderFunction)
}

fun Context.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
    builder.builderFunction()
    val dialog = builder.create()
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    dialog.show()
}

fun Fragment.navigateUp() = this.findNavController().navigateUp()
fun Fragment.navigate(direction: NavDirections) = this.findNavController().navigate(direction)

fun Context.sharedPreferences(name: String): SharedPreferences {
    val context = this.applicationContext
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    return EncryptedSharedPreferences.create(
        context,
        name,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun getAmount(amountInStr: String?): Double {
    return try {
        amountInStr?.toDouble() ?: 0.0
    } catch (e: Exception) {
        0.0
    }
}