package ie.wit.ufopedia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ie.wit.ufopedia.R
import ie.wit.ufopedia.databinding.ActivityDonateBinding
import ie.wit.ufopedia.databinding.ActivityUfoListBinding
import ie.wit.ufopedia.main.MainApp
import ie.wit.ufopedia.models.DonationModel
import timber.log.Timber

class Donate : AppCompatActivity() {

    private lateinit var donateLayout : ActivityDonateBinding
    lateinit var app: MainApp
    var totalDonated = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        donateLayout = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(donateLayout.root)
        donateLayout.toolbar.title = title
        setSupportActionBar(donateLayout.toolbar)
        app = this.application as MainApp

        donateLayout.progressBar.max = 10000

        donateLayout.amountPicker.minValue = 1
        donateLayout.amountPicker.maxValue = 1000

        donateLayout.amountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            donateLayout.paymentAmount.setText("$newVal")
        }


        donateLayout.donateButton.setOnClickListener {
            val amount = if (donateLayout.paymentAmount.text.isNotEmpty())
                donateLayout.paymentAmount.text.toString().toInt() else donateLayout.amountPicker.value
            if(totalDonated >= donateLayout.progressBar.max)
                Toast.makeText(applicationContext,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                val paymentmethod = if(donateLayout.paymentMethod.checkedRadioButtonId == R.id.Direct)
                    "Direct" else "Paypal"
                totalDonated += amount
                donateLayout.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
                donateLayout.progressBar.progress = totalDonated
                app.donationsStore.create(DonationModel(paymentmethod = paymentmethod,amount = amount))
                Timber.i("Total Donated so far $totalDonated")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_donate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_report -> { startActivity(
                Intent(this,
                Report::class.java))
            }
            R.id.item_cancel -> { startActivity(
                Intent(this,
                    UfoListActivity::class.java))
                Timber.i("Cancel pressed")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        totalDonated = app.donationsStore.findAll().sumOf { it.amount }
        donateLayout.progressBar.progress = totalDonated
        donateLayout.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
    }
}