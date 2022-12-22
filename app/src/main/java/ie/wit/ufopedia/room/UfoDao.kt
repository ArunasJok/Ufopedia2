package ie.wit.ufopedia.room

import androidx.room.*
import ie.wit.ufopedia.models.UfoModel

@Dao
interface UfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(ufo: UfoModel)

    @Query("SELECT * FROM UfoModel")
    suspend fun findAll(): List<UfoModel>

    @Query("select * from UfoModel where id = :id")
    suspend fun findById(id: Long): UfoModel

    @Update
    suspend fun update(ufo: UfoModel)

    @Delete
    suspend fun deletePlacemark(placemark: UfoModel)
}