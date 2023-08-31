package carlos.alves.bookstore

import carlos.alves.bookstore.roomRepository.BookRepository
import carlos.alves.bookstore.roomRepository.FavoriteBook
import io.mockk.coEvery
import io.mockk.mockk

class BooksRepositoryMock {

    private val favoriteBook = FavoriteBook("123")

    private val _mockBookRepository = mockk<BookRepository>() {
        coEvery { insertNewBookId(any()) } answers {
            favoriteBook
        }
        coEvery { getBookCount(any()) } answers {
            1
        }
        coEvery { deleteBookId(any()) } answers {
            favoriteBook
        }
    }

    val mockBookRepository: BookRepository
        get() = _mockBookRepository
}