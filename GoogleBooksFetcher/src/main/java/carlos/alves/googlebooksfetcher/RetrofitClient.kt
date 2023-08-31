package carlos.alves.googlebooksfetcher

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val baseURL: String) {

    private val maxResults = 20
    private var retrofitInstance: RetrofitApi? = null
    private var notAvailableString = "Not available"

    private fun createRetrofitClient() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .build()
        retrofitInstance = retrofit.create(RetrofitApi::class.java)
    }

    private fun getRetrofitClient(): RetrofitApi {
        if (retrofitInstance == null) {
            createRetrofitClient()
        }
        return retrofitInstance!!
    }

    fun redefineNotAvailableString(newString: String) {
        notAvailableString = newString
    }

    fun fetchBooksByKeyword(
        keyword: String,
        onOkResponse: ((ArrayList<Book>) -> Any),
        onNOkResponse: ((Throwable) -> Any),
        index: Int = 0
    ) {
        val retrofitClient = getRetrofitClient()

        retrofitClient.getBooksByKeyword(keyword, maxResults, index)
            .enqueue(object : Callback<SearchResult> {
                override fun onResponse(
                    call: Call<SearchResult>,
                    response: Response<SearchResult>
                ) {
                    onOkResponse.invoke(processBooks(response.body()!!))
                }

                override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                    onNOkResponse(t)
                }
            })
    }

    fun continueBookFetching(
        keyword: String,
        onOkResponse: ((ArrayList<Book>) -> Any),
        onNOkResponse: ((Throwable) -> Any),
        index: Int
    ) {
        fetchBooksByKeyword(keyword, onOkResponse, onNOkResponse, index)
    }

    private fun processBooks(searchResult: SearchResult): ArrayList<Book> {
        val allBooksList = mutableListOf<Book>()
        for (book in searchResult.items) {
            allBooksList.add(
                Book(
                    id = book.id,
                    title = book.volumeInfo.title ?: notAvailableString,
                    author = if (book.volumeInfo.authors != null) book.volumeInfo.authors[0] else notAvailableString,
                    description = book.volumeInfo.description ?: notAvailableString,
                    smallThumbnail = if (book.volumeInfo.imageLinks != null) book.volumeInfo.imageLinks.smallThumbnail else notAvailableString,
                    buyLink = book.saleInfo.buyLink ?: notAvailableString
                )
            )
        }
        return ArrayList(allBooksList.toList())
    }
}