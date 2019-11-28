package com.albanfontaine.realestatemanager2

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.albanfontaine.realestatemanager2.utils.Utils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @Test
    fun checkInternetConnectionTest() {
        // Context of the app under test.
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        assertTrue(Utils.isInternetAvailable(appContext))

        val cm: ConnectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wm: WifiManager = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.setWifiEnabled(false)
        assertFalse(Utils.isInternetAvailable(appContext))
    }
}
