package carlos.alves.bookstore.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import carlos.alves.bookstore.BookStoreApp
import carlos.alves.bookstore.roomRepository.BookRepository
import carlos.alves.googlebooksfetcher.Book
import carlos.alves.googlebooksfetcher.RetrofitClient
import java.lang.Exception

class SearchResultsViewModel(private val repository: BookRepository, private val retrofitClient: RetrofitClient) : ViewModel() {

    private val allBooks = mutableListOf<Book>()
    private var favoriteBooks = mutableListOf<Book>()

    companion object {
        var callback: ((ArrayList<Book>?, Throwable?) -> Any)? = null

        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchResultsViewModel(BookStoreApp.bookRepository, BookStoreApp.retrofitClient)
            }
        }
    }

    fun getAllBooks() = allBooks

    fun loadMoreBooks(currentIndex: Int, keyword: String, exceptionDescription: String) {
        if (callback == null) {
            throw Exception(exceptionDescription)
        }
        retrofitClient.continueBookFetching(keyword, ::processResults, ::showError, currentIndex)
    }

    private fun processResults(books: ArrayList<Book>) {
        callback!!.invoke(books, null)
    }

    private fun showError(throwable: Throwable) {
        callback!!.invoke(null, throwable)
    }

    fun setFilter(recyclerAdapter: SearchResultsAdapter, showOnlyFavorite: Boolean) {
        if (showOnlyFavorite) {
            recyclerAdapter.setBooks(favoriteBooks)
        }
        else {
            recyclerAdapter.setBooks(allBooks)
        }
    }

    fun updateFilteredBooks() {
        val filteredBooks = mutableListOf<Book>()
        for (book in allBooks) {
            val isFavorite = checkIfIsFavorite(book.id)
            if (isFavorite) {
                filteredBooks.add(book)
            }
        }
        favoriteBooks = filteredBooks
    }

    private fun checkIfIsFavorite(id: String): Boolean {
        val hasId = repository.getBookCount(id)
        if (hasId == 0) return false
        return true
    }
}