package ie.wit.ufopedia.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


import ie.wit.ufopedia.helpers.*
import java.util.*



const val JSON_FILE2 = "donations.json"
val gsonBuilder2 = GsonBuilder().setPrettyPrinting().create()
val listType2 = object : TypeToken<java.util.ArrayList<DonationModel>>() {}.type

fun generateRandomId2(): Long {
    return Random().nextLong()
}


class DonationJSONStore(private val context: Context) : DonationStore {

    var donations = mutableListOf<DonationModel>()

    init {
        if (exists(context,JSON_FILE2)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<DonationModel> {
        return donations
    }

    override fun findById(id:Long) : DonationModel? {
        val foundDonation: DonationModel? = donations.find { it.id == id }
        return foundDonation
    }

    fun findOne(id: Long) : DonationModel? {
        var foundDonation: DonationModel? = donations.find { p -> p.id == id }
        return foundDonation
    }

    override fun create(donation: DonationModel) {
        donation.id = generateRandomId2()
        donations.add(donation)
        serialize()
    }

    override fun update(donation: DonationModel) {
        var foundDonation = findOne(donation.id!!)
        if (foundDonation != null) {
            foundDonation.paymentmethod = donation.paymentmethod
            foundDonation.amount = donation.amount
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder2.toJson(donations, listType2)
        write(context, JSON_FILE2, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE2)
        donations = Gson().fromJson(jsonString, listType2)
    }
}