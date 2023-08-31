package carlos.alves.bookstore.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import carlos.alves.bookstore.IntentConstants.Companion.BOOKS_LIST
import carlos.alves.bookstore.IntentConstants.Companion.KEYWORD
import carlos.alves.bookstore.R
import carlos.alves.bookstore.searchResults.SearchResultsActivity
import carlos.alves.bookstore.databinding.ActivityMainBinding
import carlos.alves.googlebooksfetcher.Book

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels { MainViewModel.factory }
    private lateinit var keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MainViewModel.callback = ::processResponse

        binding.searchButton.setOnClickListener {
            val insertedInput = binding.searchBox.text.toString()
            if (insertedInput.isBlank()) {
                showToastError(getString(R.string.empty_input))
            } else {
                keyword = insertedInput
                viewModel.fetchBooksByKeyword(keyword, getString(R.string.viewmodel_callback))
            }
        }

        binding.exitButton.setOnClickListener {
            finish()
        }
    }

    private fun processResponse(books: ArrayList<Book>?, error: Throwable?) {
        if (error != null) {
            showToastError(error.message ?: getString(R.string.unknown_error))
        } else if(books!!.isEmpty()) {
            showToastError(getString(R.string.no_books))
        } else {
            sendToResultScreen(books)
        }
    }

    private fun sendToResultScreen(books: ArrayList<Book>) {
        val searchResultIntent = Intent(this, SearchResultsActivity::class.java)
        searchResultIntent.putExtra(BOOKS_LIST, books)
        searchResultIntent.putExtra(KEYWORD, keyword)
        startActivity(searchResultIntent)
    }

    private fun showToastError(errorDescription: String) {
        Toast.makeText(this, errorDescription, Toast.LENGTH_LONG).show()
    }
}