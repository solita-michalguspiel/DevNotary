package com.solita.devnotary.feature_notes.data.local

import com.solita.devnotary.dev_notary_db
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DbArgs()

actual fun getSqlDriver(dbArgs: DbArgs?): SqlDriver {
    return NativeSqliteDriver(dev_notary_db.Schema, "dev_notary_db")
}