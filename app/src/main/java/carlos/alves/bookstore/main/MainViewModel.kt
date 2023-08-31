package carlos.alves.bookstore.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import carlos.alves.bookstore.BookStoreApp
import carlos.alves.googlebooksfetcher.Book
import carlos.alves.googlebooksfetcher.RetrofitClient
import java.lang.Exception

class MainViewModel(private val retrofitClient: RetrofitClient) : ViewModel() {

    companion object {
        var callback: ((ArrayList<Book>?, Throwable?) -> Any)? = null

        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(BookStoreApp.retrofitClient)
            }
        }
    }

    fun fetchBooksByKeyword(keyword: String, exceptionDescription: String) {
        if (callback == null) {
            throw Exception(exceptionDescription)
        }
        retrofitClient.fetchBooksByKeyword(keyword, ::processResults, ::showError)
    }

    private fun processResults(books: ArrayList<Book>) {
        callback!!.invoke(books, null)
    }

    private fun showError(throwable: Throwable) {
        callback!!.invoke(null, throwable)
    }

}