package com.devshawon.curehealthcare.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.SystemClock
import android.provider.Settings.Global.getString
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.devshawon.curehealthcare.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.StringBuilder

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

fun getInt(amountInStr: String?): Int {
    return try {
        amountInStr?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

fun getDouble(amount : String?) : Double{
    return try {
        amount?.toDouble() ?: 0.00
    } catch (e: Exception){
        0.00
    }
}

fun getInt( data : Double?): Int {
    return try {
        data?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

object ScreenUtils {fun dpToPixel(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp.toFloat(), Resources.getSystem().displayMetrics).toInt()}}

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

fun AlertDialog.Builder.positiveButton(
    text: String,
    handleClick: () -> Unit = {}
) {
    this.setPositiveButton(text) { _, _ -> handleClick() }
}

fun AlertDialog.Builder.negativeButton(
    text: String,
    handleClick: (i: Int) -> Unit = {}
) {
    this.setNegativeButton(text) { _, i -> handleClick(i) }
}

fun Fragment.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    this.context?.showDialog(cancelable, cancelableTouchOutside, builderFunction)
}

fun changeStatusBarColor(context: Context, statusBarColor: Int) {
    val activity = context as Activity
    val window: Window = activity.window
    window.statusBarColor = ContextCompat.getColor(context, statusBarColor)
}

fun showBadge(
    context: Context?,
    bottomNavigationView: BottomNavigationView,
    @IdRes itemId: Int,
    value: String?
) {
    removeBadge(bottomNavigationView, itemId)
    val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
    val badge: View = LayoutInflater.from(context)
        .inflate(R.layout.badge_layout, bottomNavigationView, false)
    val text: TextView = badge.findViewById(R.id.badge_text_view)
    text.text = value
    itemView.addView(badge)
}

fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
    val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
    if (itemView.childCount == 3) {
        itemView.removeViewAt(2)
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}


 class WrapContentLinearLayoutManager(mContext: Context) : LinearLayoutManager(mContext) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}

fun Activity.showDialog(
    cancelable: Boolean = true, cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {

    try {
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        builder.builderFunction()
        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
        dialog.show()
    } catch (_: Exception) {
    }
}

var lastTimeClicked: Long = 0
var defaultInterval: Int = 500
class SafeClickListener(
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    override fun onClick(v: View) {
        if ((SystemClock.elapsedRealtime() - lastTimeClicked) < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}