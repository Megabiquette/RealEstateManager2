package com.albanfontaine.realestatemanager2

import com.albanfontaine.realestatemanager2.Utils.Utils.Companion.convertDollarToEuro
import com.albanfontaine.realestatemanager2.Utils.Utils.Companion.convertEuroToDollar
import org.junit.Assert.*

import org.junit.Test

class UnitTest{
    @Test
    fun conversionDollarToEuroTest(){
        val dollars = 75
        assertEquals(61, convertDollarToEuro(dollars))
    }

    @Test
    fun conversionEuroToDollarTest(){
        val euros = 75
        assertEquals(92, convertEuroToDollar(euros))
    }


}