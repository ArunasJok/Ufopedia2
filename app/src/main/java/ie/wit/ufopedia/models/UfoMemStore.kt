package ie.wit.ufopedia.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class UfoMemStore : UfoStore {

    val ufos = ArrayList<UfoModel>()

    override fun findAll(): List<UfoModel> {
        return ufos
    }

    override fun create(ufo: UfoModel) {
        ufo.id = getId()
        ufos.add(ufo)
        logAll()
    }

    override fun update(ufo: UfoModel) {
        var foundUfo: UfoModel? = ufos.find { p -> p.id == ufo.id }
        if (foundUfo != null) {
            foundUfo.title = ufo.title
            foundUfo.description = ufo.description
            foundUfo.image = ufo.image
            foundUfo.lat = ufo.lat
            foundUfo.lng = ufo.lng
            foundUfo.zoom = ufo.zoom
            logAll()
        }
    }

    private fun logAll() {
        ufos.forEach { i("$it") }
    }

    override fun delete(ufo: UfoModel) {
        ufos.remove(ufo)
    }
}