package ie.wit.ufopedia.views.ufolist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.wit.ufopedia.activities.Donate
import ie.wit.ufopedia.activities.UfoMapsActivity
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.UfoModel
import ie.wit.ufopedia.views.ufo.UfoView

class UfoListPresenter(val view: UfoListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getUfos() = app.ufos.findAll()

    fun doOpenDonate() {
        val launcherIntent = Intent(view, Donate::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doAddUfo() {
        val launcherIntent = Intent(view, UfoView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditUfo(ufo: UfoModel) {
        val launcherIntent = Intent(view, UfoView::class.java)
        launcherIntent.putExtra("ufo_edit", ufo)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowUfosMap() {
        val launcherIntent = Intent(view, UfoMapsActivity::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getUfos() }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}