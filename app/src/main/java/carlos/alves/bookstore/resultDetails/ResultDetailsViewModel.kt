package carlos.alves.bookstore.resultDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import carlos.alves.bookstore.BookStoreApp
import carlos.alves.bookstore.roomRepository.BookRepository

class ResultDetailsViewModel(private val repository: BookRepository) : ViewModel() {

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ResultDetailsViewModel(BookStoreApp.bookRepository)
            }
        }
    }

    fun checkIfIsFavorite(id: String): Boolean {
        val hasId = repository.getBookCount(id)
        if (hasId == 0) return false
        return true
    }

    fun addToFavorites(id: String) {
        repository.insertNewBookId(id)
    }

    fun removeFromFavorites(id: String) {
        repository.deleteBookId(id)
    }
}