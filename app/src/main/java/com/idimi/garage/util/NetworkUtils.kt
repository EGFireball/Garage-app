package com.idimi.garage.util

import android.content.Context
import android.net.ConnectivityManager

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork != null

//            val activeNetworkInfo = getNetworkInfo(context)
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected

}