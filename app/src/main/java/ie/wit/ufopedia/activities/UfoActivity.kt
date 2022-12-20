package ie.wit.ufopedia.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoBinding
import ie.wit.ufopedia.helpers.showImagePicker
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.Location
import ie.wit.ufopedia.models.UfoModel
import timber.log.Timber
import timber.log.Timber.i


class UfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUfoBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var ufo = UfoModel()
    // var location = Location(52.245696, -7.139102, 15f)
    lateinit var app : MainApp
    var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("UFO Activity started...")

        if (intent.hasExtra("ufo_edit")) {
            edit = true
            ufo = intent.extras?.getParcelable("ufo_edit")!!
            binding.ufoTitle.setText(ufo.title)
            binding.description.setText(ufo.description)
            binding.btnAdd.setText(R.string.save_ufo)
            Picasso.get()
                .load(ufo.image)
                .into(binding.ufoImage)
            if (ufo.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_ufo_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            ufo.title = binding.ufoTitle.text.toString()
            ufo.description = binding.description.text.toString()
            if (ufo.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_ufo_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.ufos.update(ufo.copy())
                } else {
                    app.ufos.create(ufo.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
            }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
            i("Select an image")
        }
        binding.ufoLocation.setOnClickListener {
            i ("Set location pressed")
            val location = Location(52.245696, -7.139102, 15f)
            if (ufo.zoom != 0f) {
                location.lat =  ufo.lat
                location.lng = ufo.lng
                location.zoom = ufo.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ufo, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                i("Cancel pressed")
                finish()
            }
            R.id.item_delete -> {
                i("Delete pressed")
                app.ufos.delete(ufo.copy())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            ufo.image = result.data!!.data!!
                            Picasso.get()
                                .load(ufo.image)
                                .into(binding.ufoImage)
                            binding.chooseImage.setText(R.string.change_ufo_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            ufo.lat = location.lat
                            ufo.lng = location.lng
                            ufo.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}