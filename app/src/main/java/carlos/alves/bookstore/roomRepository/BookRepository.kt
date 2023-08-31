package carlos.alves.bookstore.roomRepository

import java.util.concurrent.Callable
import java.util.concurrent.Executors

class BookRepository(database: BooksDatabase) {

    private val executor = Executors.newSingleThreadExecutor()

    private val databaseDao = database.databaseDao()

    fun getBookCount(id: String): Int = executor.submit(Callable { databaseDao.getCount(id) }).get()

    fun insertNewBookId(id: String) { executor.submit { databaseDao.insertBookId(FavoriteBook(id)) } }

    fun deleteBookId(id: String) { executor.submit { databaseDao.deleteBookId(FavoriteBook(id)) } }
}