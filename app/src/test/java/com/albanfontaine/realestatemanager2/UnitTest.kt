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

    @Test
    fun dateFormatForDBTest(){
        val date = "17/06/2019"
        assertEquals(20190617, Utils.formatDateForDB(date))
    }

    @Test
    fun getMapURLTest(){
        val location = "108 boulevard saint germain 75006 paris"
        assertEquals("https://maps.googleapis.com/maps/api/staticmap?markers=108+boulevard+saint+germain+75006+paris&zoom=15&size=300x300&scale=3&key=AIzaSyADBmQ_3NHCQjEFjqwO4R8YSe3WUc5AwcI",
            Utils.getMapUrl(location))
    }

    @Test
    fun formatPriceDollarsTest(){
        val priceDollars = 10000
        assertEquals("$10,000", Utils.formatPriceDollars(priceDollars))
    }

    @Test
    fun formatPriceEurosTest(){
        val priceEuros = 10000
        assertEquals("10 000€", Utils.formatPriceEuros(priceEuros))
    }
}