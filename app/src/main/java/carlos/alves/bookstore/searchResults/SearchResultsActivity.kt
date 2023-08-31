package carlos.alves.bookstore.searchResults

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.IntentCompat.getParcelableArrayListExtra
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import carlos.alves.bookstore.IntentConstants.Companion.AUTHOR
import carlos.alves.bookstore.IntentConstants.Companion.BOOKS_LIST
import carlos.alves.bookstore.IntentConstants.Companion.BUY_LINK
import carlos.alves.bookstore.IntentConstants.Companion.DESCRIPTION
import carlos.alves.bookstore.IntentConstants.Companion.ID
import carlos.alves.bookstore.IntentConstants.Companion.KEYWORD
import carlos.alves.bookstore.IntentConstants.Companion.THUMBNAIL
import carlos.alves.bookstore.IntentConstants.Companion.TITLE
import carlos.alves.bookstore.R
import carlos.alves.bookstore.databinding.ActivitySearchResultsBinding
import carlos.alves.bookstore.resultDetails.ResultDetailsActivity
import carlos.alves.googlebooksfetcher.Book

class SearchResultsActivity : AppCompatActivity() {

    private val binding: ActivitySearchResultsBinding by lazy { ActivitySearchResultsBinding.inflate(layoutInflater) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.book_results) }
    private val viewModel: SearchResultsViewModel by viewModels { SearchResultsViewModel.factory }
    private lateinit var recyclerAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SearchResultsViewModel.callback = ::processResponse

        val booksListByIntent = getParcelableArrayListExtra(intent, BOOKS_LIST, Book::class.java)
        val keyword = intent.getStringExtra(KEYWORD)
        viewModel.getAllBooks().addAll(booksListByIntent!!)
        viewModel.updateFilteredBooks()

        recyclerAdapter = SearchResultsAdapter(this)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerAdapter.setBooks(viewModel.getAllBooks())

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == 0) return
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastIndex = linearLayoutManager.itemCount - 1
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == lastIndex) {
                    viewModel.loadMoreBooks(lastIndex, keyword!!, getString(R.string.viewmodel_callback))
                }
            }
        })

        recyclerAdapter.getCurrentBook().observe(this) {
            val detailsIntent = Intent(this, ResultDetailsActivity::class.java)
            detailsIntent.putExtra(ID, it.id)
            detailsIntent.putExtra(TITLE, it.title)
            detailsIntent.putExtra(AUTHOR, it.author)
            detailsIntent.putExtra(DESCRIPTION, it.description)
            detailsIntent.putExtra(THUMBNAIL, it.smallThumbnail)
            detailsIntent.putExtra(BUY_LINK, it.buyLink)
            startActivity(detailsIntent)
        }

        binding.filterSwitch.setOnClickListener {
            viewModel.setFilter(recyclerAdapter, binding.filterSwitch.isChecked)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun processResponse(books: ArrayList<Book>?, error: Throwable?) {
        if (error != null) {
            Toast.makeText(this, error.message ?: getString(R.string.unknown_error), Toast.LENGTH_LONG).show()
        } else {
            viewModel.getAllBooks().addAll(books!!)
            recyclerAdapter.setBooks(viewModel.getAllBooks())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFilteredBooks()
    }
}