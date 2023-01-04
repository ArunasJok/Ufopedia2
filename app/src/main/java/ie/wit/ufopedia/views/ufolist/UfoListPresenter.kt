package ie.wit.ufopedia.views.ufolist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.ufopedia.activities.Donate
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.UfoModel
import ie.wit.ufopedia.views.login.LoginView
import ie.wit.ufopedia.views.map.UfoMapView
import ie.wit.ufopedia.views.ufo.UfoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UfoListPresenter(val view: UfoListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    // private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        // registerMapCallback()
        registerRefreshCallback()
        registerEditCallback()
    }

    suspend fun getUfos() = app.ufos.findAll()

    fun doOpenDonate() {
        val launcherIntent = Intent(view, Donate::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doAddUfo() {
        val launcherIntent = Intent(view, UfoView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditUfo(ufo: UfoModel) {
        val launcherIntent = Intent(view, UfoView::class.java)
        launcherIntent.putExtra("ufo_edit", ufo)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowUfosMap() {
        val launcherIntent = Intent(view, UfoMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { GlobalScope.launch(Dispatchers.Main){
                app.ufos.clear()
                getUfos()
            } }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }


    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.ufos.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
}