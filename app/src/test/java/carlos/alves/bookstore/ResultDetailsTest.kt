package carlos.alves.bookstore

import carlos.alves.bookstore.resultDetails.ResultDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResultDetailsTest {

    private val mockBookRepository = BooksRepositoryMock().mockBookRepository
    private lateinit var viewModel: ResultDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ResultDetailsViewModel(mockBookRepository)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `Check if is favorite - with success`() {
        runBlocking {
            val isFavorite = viewModel.checkIfIsFavorite("123")
            Assert.assertEquals(true, isFavorite)
        }
    }
}