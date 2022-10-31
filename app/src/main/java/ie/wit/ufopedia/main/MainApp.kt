package ie.wit.ufopedia.main

import android.app.Application
import ie.wit.ufopedia.models.*
import mu.KotlinLogging
import timber.log.Timber
import timber.log.Timber.i

private val logger = KotlinLogging.logger {}

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
        logger.info { "Launching UFOpedia App" }
    }
}