package ie.wit.ufopedia.views.ufo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoBinding
import ie.wit.ufopedia.models.Location
import ie.wit.ufopedia.models.UfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i

class UfoView : AppCompatActivity() {

    private lateinit var binding: ActivityUfoBinding
    lateinit var presenter: UfoPresenter
    lateinit var map: GoogleMap
    var ufo = UfoModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = UfoPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheUfo(binding.ufoTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        /*binding.ufoLocation.setOnClickListener {
            presenter.cacheUfo(binding.ufoTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }*/

        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ufo, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.ufoTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_ufo_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doAddOrSave(
                            binding.ufoTitle.text.toString(),
                            binding.description.text.toString()
                        )
                    }
                }
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO){
                    presenter.doDelete()
                }
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showUfo(ufo: UfoModel) {
        if (binding.ufoTitle.text.isEmpty()) binding.ufoTitle.setText(ufo.title)
        if (binding.description.text.isEmpty())  binding.description.setText(ufo.description)

        if (ufo.image != "") {
            Picasso.get()
                .load(ufo.image)
                .into(binding.ufoImage)

            binding.chooseImage.setText(R.string.change_ufo_image)
        }
        this.showLocation(ufo.location)
    }
    private fun showLocation (loc: Location){
        binding.lat.setText("%.6f".format(loc.lat))
        binding.lng.setText("%.6f".format(loc.lng))
    }

    fun updateImage(image: String){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.ufoImage)
        binding.chooseImage.setText(R.string.change_ufo_image)
    }



    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
        presenter.doRestartLocationUpdates()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }
}

