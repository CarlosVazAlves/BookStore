package carlos.alves.bookstore.roomRepository

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Entity(tableName = "BooksId")
data class FavoriteBook(@PrimaryKey val id: String)

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookId(book: FavoriteBook)

    @Delete
    fun deleteBookId(book: FavoriteBook)

    @Query("SELECT COUNT(id) FROM BooksId WHERE Id = :id")
    fun getCount(id: String) : Int
}

@Database(entities = [FavoriteBook::class], version = 1)
@TypeConverters(Converters::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun databaseDao() : BookDao
}

class Converters {
    @TypeConverter
    fun toFavoriteBook(id: String): FavoriteBook {
        val favoriteBookType: Type = object : TypeToken<FavoriteBook?>() {}.type
        return Gson().fromJson(id, favoriteBookType)
    }

    @TypeConverter
    fun fromFavoriteBook(book: FavoriteBook): String {
        return Gson().toJson(book)
    }
}