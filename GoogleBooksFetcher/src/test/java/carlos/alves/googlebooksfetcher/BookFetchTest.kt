package carlos.alves.googlebooksfetcher

import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class BookFetchTest {

    @get:Rule
    val testRule = MockGoogleServer()

    private val searchResult = SearchResult(listOf(
        BookItem(
            "123",
            VolumeInfo(
                "ExpectedTitle",
                listOf("ExpectedAuthors"),
                "ExpectedDescription",
                ImageLinks(
                    "ExpectedSmallThumbnail",
                    "ExpectedThumbnail"
                )
            ),
            SaleInfo("ExpectedBuyLink")
        )
    ))

    @Test
    fun `Get books - with success`(): Unit = runBlocking {
        val mockServer = testRule.server
        mockServer.enqueue(
            response = MockResponse()
                .setHeader("content-type", "application/json")
                .setBody(
                    GsonBuilder().create().toJson(searchResult)
                )
        )
        RetrofitClient(mockServer.url("/").toUrl().toString()).fetchBooksByKeyword("ExpectedKeyword", ::getBooks, ::stub)
    }

    private fun getBooks(books: ArrayList<Book>) {
        val book = books[0]
        Assert.assertEquals("123", book.id)
        Assert.assertEquals("ExpectedSmallThumbnail", book.smallThumbnail)
    }

    private fun stub(throwable: Throwable) {}
}