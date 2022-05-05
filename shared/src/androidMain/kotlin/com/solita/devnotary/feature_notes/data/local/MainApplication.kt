package com.solita.devnotary.feature_notes.data.local

import android.app.Application
import android.content.Context
import com.solita.devnotary.dev_notary_db
import com.solita.devnotary.di.dbArgs
import com.solita.devnotary.di.di
import com.squareup.sqldelight.android.AndroidSqliteDriver

import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindSingleton
import org.kodein.di.compose.androidContextDI
import org.kodein.di.instance

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        dbArgs = DbArgs(this)
    }
}
