package ie.wit.ufopedia.models

import timber.log.Timber

var lastIdmem = 0L

internal fun getIdmem(): Long {
    return lastIdmem++
}

class DonationMemStore : DonationStore {

    val donations = ArrayList<DonationModel>()

    override fun findAll(): List<DonationModel> {
        return donations
    }

    override fun findById(id:Long) : DonationModel? {
        val foundDonation: DonationModel? = donations.find { it.id == id }
        return foundDonation
    }

    override fun create(donation: DonationModel) {
        donation.id = getIdmem()
        donations.add(donation)
        logAll()
    }

    fun logAll() {
        Timber.v("** Donations List **")
        donations.forEach { Timber.v("Donate ${it}") }
    }
}