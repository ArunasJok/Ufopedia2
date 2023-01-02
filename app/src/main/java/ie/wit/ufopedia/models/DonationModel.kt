package ie.wit.ufopedia.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonationModel(var id: Long = 0,
                         var paymentmethod: String = "N/A",
                         var amount: Int = 0) : Parcelable




