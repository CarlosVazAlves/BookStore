package carlos.alves.googlebooksfetcher
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchResult(val items: List<BookItem>)

data class BookItem(val id: String, val volumeInfo: VolumeInfo, val saleInfo: SaleInfo)

data class VolumeInfo(val title: String?, val authors: List<String>?, val description: String?, val imageLinks: ImageLinks?)

data class SaleInfo(val buyLink: String?)

data class ImageLinks(val smallThumbnail: String, val thumbnail: String)

@Parcelize
data class Book(val id: String, val title: String, val author: String, val description: String?, val smallThumbnail: String?, val buyLink: String?) : Parcelable