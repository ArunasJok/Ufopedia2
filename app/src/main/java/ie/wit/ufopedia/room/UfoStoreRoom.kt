package ie.wit.ufopedia.room

import android.content.Context
import androidx.room.Room
import ie.wit.ufopedia.models.UfoModel
import ie.wit.ufopedia.models.UfoStore

class UFoStoreRoom(val context: Context) : UfoStore {

    var dao: UfoDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.ufoDao()
    }

    override suspend fun findAll(): List<UfoModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): UfoModel? {
        return dao.findById(id)
    }

    override suspend fun create(ufo: UfoModel) {
        dao.create(ufo)
    }

    override suspend fun update(ufo: UfoModel) {
        dao.update(ufo)
    }

    override suspend fun delete(ufo: UfoModel) {
        dao.deleteUfo(ufo)
    }

    override suspend fun clear(){
    }
}