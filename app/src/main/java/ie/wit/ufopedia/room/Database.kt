package ie.wit.ufopedia.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.ufopedia.helpers.Converters
import ie.wit.ufopedia.models.UfoModel

@Database(entities = arrayOf(UfoModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun ufoDao(): UfoDao
}