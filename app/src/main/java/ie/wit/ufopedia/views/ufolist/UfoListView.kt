package ie.wit.ufopedia.views.ufolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import ie.wit.ufopedia.R
import ie.wit.ufopedia.adapters.UfoAdapter
import ie.wit.ufopedia.adapters.UfoListener
import ie.wit.ufopedia.databinding.ActivityUfoListBinding
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.UfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class UfoListView : AppCompatActivity(), UfoListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityUfoListBinding
    lateinit var presenter: UfoListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUfoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }
        presenter = UfoListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadUfos()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        Timber.i("recyclerView onResume")
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddUfo() }
            R.id.item_map -> { presenter.doShowUfosMap() }
            R.id.item_donate -> { presenter.doOpenDonate() }
            R.id.item_logout -> { GlobalScope.launch(Dispatchers.IO) {
                presenter.doLogout()
            } }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUfoClick(ufo: UfoModel) {
        presenter.doEditUfo(ufo)

    }

    private fun loadUfos() {
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                UfoAdapter(presenter.getUfos(), this@UfoListView)
        }
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}