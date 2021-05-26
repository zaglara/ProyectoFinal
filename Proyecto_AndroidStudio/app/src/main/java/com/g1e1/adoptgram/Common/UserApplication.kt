package com.g1e1.adoptgram.Common

import android.app.Application
import com.g1e1.adoptgram.Data.DataBaseHelper

class UserApplication : Application() {

    companion object {
        lateinit var prefs: Prefs
        lateinit var dbHelper: DataBaseHelper
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        dbHelper =  DataBaseHelper(applicationContext)
    }

}