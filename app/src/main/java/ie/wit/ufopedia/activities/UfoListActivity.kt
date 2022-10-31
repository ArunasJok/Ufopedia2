package ie.wit.ufopedia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.ufopedia.R
import ie.wit.ufopedia.adapters.UfoAdapter
import ie.wit.ufopedia.adapters.UfoListener
import ie.wit.ufopedia.databinding.ActivityUfoListBinding
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.UfoModel


class UfoListActivity : AppCompatActivity(), UfoListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityUfoListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUfoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        // binding.recyclerView.adapter = UfoAdapter(app.ufos)
        // binding.recyclerView.adapter = UfoAdapter(app.ufos.findAll(),this)
        loadUfos()
        registerRefreshCallback()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, UfoActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_donate -> {
                val launcherIntent = Intent(this, Donate::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.action_report -> {
                val launcherIntent = Intent(this, Report::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onUfoClick(ufo: UfoModel) {
        val launcherIntent = Intent(this, UfoActivity::class.java)
        launcherIntent.putExtra("ufo_edit", ufo)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadUfos() }
    }

    private fun loadUfos() {
        showUfos(app.ufos.findAll())
    }

    fun showUfos (ufos: List<UfoModel>) {
        binding.recyclerView.adapter = UfoAdapter(ufos, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}



