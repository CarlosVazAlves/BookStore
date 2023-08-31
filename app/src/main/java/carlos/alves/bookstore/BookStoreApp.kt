package carlos.alves.bookstore

import android.app.Application
import androidx.room.Room
import carlos.alves.bookstore.roomRepository.BookRepository
import carlos.alves.bookstore.roomRepository.BooksDatabase
import carlos.alves.googlebooksfetcher.RetrofitClient

class BookStoreApp : Application() {

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/"
        lateinit var bookRepository: BookRepository
        lateinit var retrofitClient: RetrofitClient
    }

    override fun onCreate() {
        super.onCreate()
        retrofitClient = RetrofitClient(BASE_URL)
        retrofitClient.redefineNotAvailableString(getString(R.string.na))

        val database = Room.databaseBuilder(applicationContext, BooksDatabase::class.java, "book_db")
            .fallbackToDestructiveMigration()
            .build()

        bookRepository = BookRepository(database)
    }
}