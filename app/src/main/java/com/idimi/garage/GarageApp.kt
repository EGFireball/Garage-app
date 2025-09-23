package com.idimi.garage

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GarageApp: Application() {

    companion object {

        @Throws(java.lang.Exception::class)
        fun getApplicationUsingReflection(): Application {
            return Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication").invoke(null) as Application
        }

        @Synchronized
        fun getAppContext(): Context {
            return getApplicationUsingReflection().applicationContext
            //return mContext
        }
    }
}