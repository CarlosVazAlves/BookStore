package carlos.alves.googlebooksfetcher

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitApi {

    @GET("books/v1/volumes")
    @Headers("Accept: application/json")
    fun getBooksByKeyword(
        @Query("q") q: String,
        @Query("maxResults") maxResults: Int,
        @Query("startIndex") startIndex: Int)
            : Call<SearchResult>
}