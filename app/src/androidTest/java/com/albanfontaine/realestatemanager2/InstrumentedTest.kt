package com.albanfontaine.realestatemanager2

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @Test
    fun checkInternetConnectionTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertTrue(Utils.isInternetAvailable(appContext))
    }
}
