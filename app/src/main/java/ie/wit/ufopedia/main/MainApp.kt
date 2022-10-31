package ie.wit.ufopedia.main

import android.app.Application
import ie.wit.ufopedia.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var donationsStore: DonationStore
    lateinit var ufos: UfoStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // ufos = UfoMemStore()
        ufos = UfoJSONStore(applicationContext)
        donationsStore = DonationMemStore()
        i("UFOpedia has started!")
    }
}