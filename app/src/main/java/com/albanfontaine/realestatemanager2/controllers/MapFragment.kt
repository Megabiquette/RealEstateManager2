package com.albanfontaine.realestatemanager2.controllers


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private var mDb: AppDatabase? = null
	private lateinit var mMap: GoogleMap
	private lateinit var mProperties: List<Property>

	override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_map, container, false)
		val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
		getProperties()

		return view
	}

	override fun onMapReady(googleMap: GoogleMap?) {
		mMap = googleMap!!
		mMap.isBuildingsEnabled = true
		this.zoomOnMyLocation(mMap)
		Places.initialize(requireContext(), resources.getString(R.string.googlemaps_api))
		mMap.setMapStyle(MapStyleOptions(resources.getString(R.string.map_style)))
		mMap.setOnMarkerClickListener(this)
		this.addMarkers(requireContext())
	}

	private fun zoomOnMyLocation(map: GoogleMap) {
		// Checks for permission
		if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(requireActivity() ,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.MY_PERMISSIONS_REQUEST_LOCATION)
		} else {
			mMap.isMyLocationEnabled = true
			// Zoom the map on current location
			val locationManager =
				requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
			val location = locationManager.getLastKnownLocation(
				locationManager.getBestProvider(
					Criteria(),
					false
				)
			)
			if (location != null) {
				val cameraPosition = CameraPosition.Builder()
					.target(LatLng(location.latitude, location.longitude))
					.zoom(16f)
					.build()
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
			}
		}
	}

	private fun addMarkers(context: Context){
		val geocoder = Geocoder(context)
		for (property: Property in mProperties){
			if(!property.address.equals("")){
				val addresses: List<Address> = geocoder.getFromLocationName(property.address, 1)
				val marker: Marker = mMap.addMarker(MarkerOptions()
					.position(LatLng(addresses.get(0).latitude, addresses.get(0).longitude)))
				marker.tag = property.id
			}
		}
	}

	override fun onMarkerClick(marker: Marker?): Boolean {
		val intent = Intent(requireContext(), PropertyCardFragment::class.java)
		intent.putExtra(Constants.PROPERTY_ID, marker?.tag as Long)
		startActivity(intent)

		return false
	}

	private fun getProperties(){
		mDb = AppDatabase.getInstance(requireContext())
		val executor: Executor = Executors.newSingleThreadExecutor()
		executor.execute{
			mProperties = mDb?.propertyDAO()?.getProperties()!!
		}
	}
}
