package ie.wit.ufopedia.views.ufo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoBinding
import ie.wit.ufopedia.helpers.checkLocationPermissions
import ie.wit.ufopedia.helpers.createDefaultLocationRequest
import ie.wit.ufopedia.helpers.showImagePicker
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.Location
import ie.wit.ufopedia.models.UfoModel
import ie.wit.ufopedia.views.location.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class UfoPresenter(private val view: UfoView) {

    var map: GoogleMap? = null
    var ufo = UfoModel()
    var app: MainApp = view.application as MainApp
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private val locationRequest = createDefaultLocationRequest()
    //var binding: ActivityUfoBinding = ActivityUfoBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("ufo_edit")) {
            edit = true
            ufo = view.intent.extras?.getParcelable<UfoModel>("ufo_edit")!!
            view.showUfo(ufo)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            ufo.lat = location.lat
            ufo.lng = location.lng
        }
    }

    fun doAddOrSave(title: String, description: String) {
        ufo.title = title
        ufo.description = description
        if (edit) {
            app.ufos.update(ufo)
        } else {
            app.ufos.create(ufo)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.ufos.delete(ufo)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (ufo.zoom != 0f) {
            location.lat =  ufo.lat
            location.lng = ufo.lng
            location.zoom = ufo.zoom
            locationUpdate(ufo.lat, ufo.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(ufo.lat, ufo.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        ufo.lat = lat
        ufo.lng = lng
        ufo.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(ufo.title).position(LatLng(ufo.lat, ufo.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(ufo.lat, ufo.lng), ufo.zoom))
        view?.showUfo(ufo)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun cacheUfo (title: String, description: String) {
        ufo.title = title;
        ufo.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            ufo.image = result.data!!.data!!
                            view.updateImage(ufo.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            ufo.lat = location.lat
                            ufo.lng = location.lng
                            ufo.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}