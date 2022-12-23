package ie.wit.ufopedia.models

interface UfoStore {
    suspend fun findAll(): List<UfoModel>
    suspend fun create(ufo: UfoModel)
    suspend fun update(ufo: UfoModel)
    suspend fun delete(ufo: UfoModel)
    suspend fun findById(id:Long) : UfoModel?
    suspend fun clear()
}