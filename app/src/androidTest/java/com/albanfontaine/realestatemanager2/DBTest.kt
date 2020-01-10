package com.albanfontaine.realestatemanager2

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.models.PropertyAndMedias
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DBTest{
	private lateinit var db: AppDatabase

	@Before
	fun createDb(){
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
	}

	@After
	@Throws(IOException::class)
	fun closeDb(){
		db.close()
	}

	@Test
	@Throws(Exception::class)
	fun insertAndGetproperty(){
		// Create a property and a media associated to it
		val property: Property = Property(0, null, 300000, 75, null, null, null, null, null, null, null, true,null, null, null)
		val propId = db.propertyDAO().insertProperty(property)
		val media: Media = Media(0, "URITest", "DescriptionTest", propId)
		val mediaId = db.mediaDAO().insertMedia(media)

		// Get the property and test it
		val properties: List<PropertyAndMedias> = db.propertyAndMediasDAO().getProperties()

		assertEquals(properties[0].property?.id, propId)
		assertEquals(properties[0].property?.price, 300000)
		assertEquals(properties[0].property?.surface, 75)
		assertEquals(properties[0].medias[0].id, mediaId)
		assertEquals(properties[0].medias[0].uri,  "URITest")
		assertEquals(properties[0].medias[0].description,  "DescriptionTest")
		assertEquals(properties[0].medias[0].associatedPropertyId, propId)

	}
}