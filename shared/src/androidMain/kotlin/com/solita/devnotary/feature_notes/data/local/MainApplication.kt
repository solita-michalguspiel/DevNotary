package com.solita.devnotary.feature_notes.data.local

import android.app.Application
import com.solita.devnotary.utils.dbArgs

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        dbArgs = DbArgs(this)
    }
}
