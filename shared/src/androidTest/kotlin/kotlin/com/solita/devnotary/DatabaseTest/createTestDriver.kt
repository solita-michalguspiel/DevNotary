package com.solita.devnotary.DatabaseTest

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.solita.devnotary.dev_notary_db
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

internal actual fun createTestDriver(): SqlDriver {
    val app = ApplicationProvider.getApplicationContext<Application>()
    println("CREATED SOMETHING!!!")
    return AndroidSqliteDriver(dev_notary_db.Schema, app, null)
}

@RunWith(RobolectricTestRunner::class)
actual abstract class RobolectricTests