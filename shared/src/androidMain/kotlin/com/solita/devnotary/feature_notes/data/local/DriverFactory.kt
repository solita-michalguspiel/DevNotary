package com.solita.devnotary.feature_notes.data.local

import android.content.Context
import com.solita.devnotary.dev_notary_db
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DbArgs(
    var context : Context
)

actual fun getSqlDriver(dbArgs: DbArgs?): SqlDriver {
   return AndroidSqliteDriver(dev_notary_db.Schema,dbArgs!!.context,"dev_notary_db")
}