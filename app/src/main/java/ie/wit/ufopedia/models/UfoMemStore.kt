package ie.wit.ufopedia.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class UfoMemStore : UfoStore {

    val ufos = ArrayList<UfoModel>()

    override suspend fun findAll(): List<UfoModel> {
        return ufos
    }

    override suspend fun create(ufo: UfoModel) {
        ufo.id = getId()
        ufos.add(ufo)
        logAll()
    }

    override suspend fun update(ufo: UfoModel) {
        var foundUfo: UfoModel? = ufos.find { p -> p.id == ufo.id }
        if (foundUfo != null) {
            foundUfo.title = ufo.title
            foundUfo.description = ufo.description
            foundUfo.image = ufo.image
            foundUfo.location = ufo.location
            logAll()
        }
    }

    private fun logAll() {
        ufos.forEach { i("$it") }
    }

    override suspend fun delete(ufo: UfoModel) {
        ufos.remove(ufo)
    }

    override suspend fun findById(id:Long) : UfoModel? {
        val foundUfo: UfoModel? = ufos.find { it.id == id }
        return foundUfo
    }

    override suspend fun clear(){
        ufos.clear()
    }
}