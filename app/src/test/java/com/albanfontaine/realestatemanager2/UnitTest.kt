package com.albanfontaine.realestatemanager2

import com.albanfontaine.realestatemanager2.utils.Utils
import org.junit.Assert.*

import org.junit.Test
import java.util.*

class UnitTest{
    @Test
    fun conversionDollarToEuroTest(){
        val dollars = 75
        assertEquals(61, Utils.convertDollarToEuro(dollars))
    }

    @Test
    fun conversionEuroToDollarTest(){
        val euros = 75
        assertEquals(92, Utils.convertEuroToDollar(euros))
    }

    @Test
    fun dateFormatTest(){
        val calendar = Calendar.getInstance()
        calendar.set(2019, 4, 24) // 24th of May 2019
        val date = calendar.time

        assertEquals("24/05/2019", Utils.formatDate(date))
    }

}