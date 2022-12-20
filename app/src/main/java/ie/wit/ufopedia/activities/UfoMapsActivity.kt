package ie.wit.ufopedia.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.GoogleMap
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityUfoMapsBinding
import ie.wit.ufopedia.databinding.ContentUfoMapsBinding

class UfoMapsActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUfoMapsBinding
    private lateinit var contentBinding: ContentUfoMapsBinding
    lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUfoMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentUfoMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
    }
}
