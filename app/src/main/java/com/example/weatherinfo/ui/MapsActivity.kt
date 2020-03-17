package com.example.weatherinfo.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherinfo.R
import com.example.weatherinfo.model.UserLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var locationList = arrayListOf<UserLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        actionBar?.hide()

        val bundle = intent.extras
        locationList =
            bundle?.getParcelableArrayList<UserLocation>("locationList") as ArrayList<UserLocation>
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activityMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val zoom = CameraUpdateFactory.zoomTo(15f)
        for (locations in locationList) {
            val point = LatLng(locations.latitude, locations.longitude)
            addMarker(point, locations.name)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        }
        mMap.animateCamera(zoom)
    }

    private fun addMarker(point: LatLng, name: String) {
        mMap.addMarker(MarkerOptions().position(point).title(name))
    }
}
