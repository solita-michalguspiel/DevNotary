package com.solita.devnotary.feature_notes.data.local

import com.squareup.sqldelight.db.SqlDriver

expect class DbArgs

expect fun getSqlDriver(dbArgs: DbArgs) : SqlDriver
