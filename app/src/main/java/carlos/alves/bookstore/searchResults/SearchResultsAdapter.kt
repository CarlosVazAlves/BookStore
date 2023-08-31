package carlos.alves.bookstore.searchResults

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import carlos.alves.bookstore.R
import carlos.alves.googlebooksfetcher.Book
import coil.load

class SearchResultsAdapter internal constructor(context: Context) : RecyclerView.Adapter<SearchResultsAdapter.ItemViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var books = mutableListOf<Book>()
    private var currentBook: MutableLiveData<Book> = MutableLiveData()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.list_book_title)
        val bookThumbnail: ImageView = itemView.findViewById(R.id.list_book_thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.search_result_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bookTitle.text = books[position].title
        holder.bookThumbnail.load(books[position].smallThumbnail)
        holder.itemView.setOnClickListener {
            currentBook.value = Book(
                id = books[position].id,
                title = books[position].title,
                author = books[position].author,
                description = books[position].description,
                smallThumbnail = books[position].smallThumbnail,
                buyLink = books[position].buyLink,
            )
        }
    }

    override fun getItemCount(): Int = books.size

    fun getCurrentBook(): MutableLiveData<Book> {
        return currentBook
    }

    fun setBooks(books: MutableList<Book>) {
        this.books = books
        notifyDataSetChanged()
    }
}