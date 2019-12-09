package com.albanfontaine.realestatemanager2

import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.runner.AndroidJUnit4
import com.albanfontaine.realestatemanager2.utils.Utils

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @Test
    fun checkInternetConnectionTest() {
        //val appContext = ApplicationProvider.getApplicationContext<Context>()
        val connectivityManager: ConnectivityManager = mock(ConnectivityManager::class.java)
        val networkInfo: NetworkInfo = mock(NetworkInfo::class.java)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)

		// Testing when internet is on
        `when`(networkInfo.isConnected).thenReturn(true)
        assertTrue(Utils.isInternetAvailable(connectivityManager))

		// Testing when internet is off
        `when`(networkInfo.isConnected).thenReturn(false)
        assertFalse(Utils.isInternetAvailable(connectivityManager))
    }
}
