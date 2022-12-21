package ie.wit.ufopedia.activities

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoMapsBinding
import ie.wit.ufopedia.databinding.ContentUfoMapsBinding
import ie.wit.ufopedia.main.MainApp

class UfoMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUfoMapsBinding
    private lateinit var contentBinding: ContentUfoMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityUfoMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        contentBinding = ContentUfoMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }
    fun configureMap() {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)
        app.ufos.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val ufo = app.ufos.findById(tag)
        contentBinding.currentTitle.text = ufo!!.title
        contentBinding.currentDescription.text = ufo!!.description
        //imageView.setImageBitmap(readImageFromPath(this@UfoMapsActivity, ufo.image))
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

}
