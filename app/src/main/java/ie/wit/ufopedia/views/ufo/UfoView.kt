package ie.wit.ufopedia.views.ufo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoBinding
import ie.wit.ufopedia.models.UfoModel
import timber.log.Timber.i

class UfoView : AppCompatActivity() {

    private lateinit var binding: ActivityUfoBinding
    private lateinit var presenter: UfoPresenter
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

        binding.ufoLocation.setOnClickListener {
            presenter.cacheUfo(binding.ufoTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
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
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showUfo(ufo: UfoModel) {
        binding.ufoTitle.setText(ufo.title)
        binding.description.setText(ufo.description)

        Picasso.get()
            .load(ufo.image)
            .into(binding.ufoImage)
        if (ufo.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_ufo_image)
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.ufoImage)
        binding.chooseImage.setText(R.string.change_ufo_image)
    }

}