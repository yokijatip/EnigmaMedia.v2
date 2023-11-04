package com.enigma.enigmamediav2.view.maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.enigma.enigmamediav2.R
import com.enigma.enigmamediav2.databinding.ActivityUserLocation2Binding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.viewModel.maps.UserLocationViewModel
import com.enigma.enigmamediav2.viewModel.maps.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class UserLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserLocation2Binding
    private lateinit var userLocationViewModel: UserLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLocation2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //        ViewModel
        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        userLocationViewModel =
            ViewModelProvider(this, viewModelFactory)[UserLocationViewModel::class.java]

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userLocationViewModel.getUserLocation()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        userMarker()
    }

    private fun userMarker() {
        val indonesia = LatLng(-5.9305961, 108.0058252)

        userLocationViewModel.storyListLiveData.observe(this) { stories ->

            mMap.clear()

            stories?.forEach { story ->
                val latLng = LatLng(story.lat as Double, story.lon as Double)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 4f))
    }
}