package com.example.bookflix.data


import com.example.bookflix.model.BooksApiResponse
import com.example.bookflix.model.Item
import com.example.bookflix.network.BooksApiService

interface BooksRepository {
    suspend fun getBooks(query: String): List<Item>
}

/**
 * Network Implementation of repository that retrieves data from underlying data source.
 */
class DefaultBooksRepository(
    private val booksApiService: BooksApiService
) : BooksRepository {
    override suspend fun getBooks(query: String): List<Item> {
        val response = booksApiService.getBooks(query)
        return response.items
    }
}