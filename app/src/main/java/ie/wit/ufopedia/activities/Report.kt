package ie.wit.ufopedia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.ufopedia.R
import ie.wit.ufopedia.adapters.DonationAdapter
import ie.wit.ufopedia.databinding.ActivityReportBinding
import ie.wit.ufopedia.databinding.ActivityDonateBinding
import ie.wit.ufopedia.main.MainApp

class Report : AppCompatActivity() {

    lateinit var app: MainApp
    lateinit var reportLayout : ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportLayout = ActivityReportBinding.inflate(layoutInflater)
        setContentView(reportLayout.root)
        reportLayout.toolbar.title = title
        setSupportActionBar(reportLayout.toolbar)

        app = this.application as MainApp
        reportLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        reportLayout.recyclerView.adapter = DonationAdapter(app.donationsStore.findAll())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_report, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_donate -> { startActivity(
                Intent(this,
                    Donate::class.java)
            )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}