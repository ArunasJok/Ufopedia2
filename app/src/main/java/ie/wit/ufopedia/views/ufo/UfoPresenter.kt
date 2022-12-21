package ie.wit.ufopedia.views.ufo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoBinding
import ie.wit.ufopedia.helpers.showImagePicker
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.Location
import ie.wit.ufopedia.models.UfoModel
import ie.wit.ufopedia.views.location.EditLocationView
import timber.log.Timber

class UfoPresenter(private val view: UfoView) {

    var ufo = UfoModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityUfoBinding = ActivityUfoBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("ufo_edit")) {
            edit = true
            ufo = view.intent.extras?.getParcelable("ufo_edit")!!
            view.showUfo(ufo)
        }
        registerImagePickerCallback()
        registerMapCallback()
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
        val location = Location(52.245696, -7.139102, 15f)
        if (ufo.zoom != 0f) {
            location.lat =  ufo.lat
            location.lng = ufo.lng
            location.zoom = ufo.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
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
}