package ie.wit.ufopedia.models

interface UfoStore {
    fun findAll(): List<UfoModel>
    fun create(ufo: UfoModel)
    fun update(ufo: UfoModel)
    fun delete(ufo: UfoModel)
    fun findById(id:Long) : UfoModel?
}