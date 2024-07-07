package com.example.bookflix.network


import com.example.bookflix.model.BooksApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BooksApiResponse
}