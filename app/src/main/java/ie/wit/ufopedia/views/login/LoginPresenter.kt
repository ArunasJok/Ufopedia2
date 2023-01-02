package ie.wit.ufopedia.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.UfoFireStore
import ie.wit.ufopedia.views.ufolist.UfoListView


class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var app: MainApp = view.application as MainApp
    var fireStore: UfoFireStore? = null


    init{
        registerLoginCallback()
        if (app.ufos is UfoFireStore) {
            fireStore = app.ufos as UfoFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchUfos {
                        view?.hideProgress()
                        val launcherIntent = Intent(view, UfoListView::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                } else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, UfoListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view?.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }

    }


    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchUfos {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, UfoListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}