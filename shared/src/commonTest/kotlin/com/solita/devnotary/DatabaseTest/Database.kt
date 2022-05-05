package com.solita.devnotary.DatabaseTest

import com.squareup.sqldelight.db.SqlDriver


internal expect fun createTestDriver(): SqlDriver

expect abstract class RobolectricTests()