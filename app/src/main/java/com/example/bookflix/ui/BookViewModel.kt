package com.example.bookflix.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookflix.BookflixApplication
import com.example.bookflix.data.BooksRepository
import com.example.bookflix.model.Item
import com.example.bookflix.model.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

sealed interface BooksUiState { //for main screen
    data class Success(
        val horrorBooks: List<Item>,
        val romanceBooks: List<Item>,
        val bookTypes: List<List<Item>>,
        val typeNames: List<String>
    ) : BooksUiState

    data object Error : BooksUiState
    data object Loading : BooksUiState
}

sealed interface BooksUiStateSearch { //for search book screen
    data class Success(
        val searchedBooks: List<Item>
    ) : BooksUiStateSearch

    data object Error : BooksUiStateSearch
    data object Loading : BooksUiStateSearch
}

class BookViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    var booksUiStateSearch: BooksUiStateSearch by mutableStateOf(BooksUiStateSearch.Loading)
        private set

    private val genres = listOf("horror", "romance", "mystery", "dystopian", "poetry", "comic")

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        booksApiLaunchCall()
    }

    fun selectBook(book: Item) {
        _uiState.update { currentState ->
            currentState.copy(
                bookSelected = book
            )
        }
    }

    fun searchBook(name: String) {
        viewModelScope.launch {
            booksUiStateSearch = BooksUiStateSearch.Loading
            booksUiStateSearch = fetchBooksByName(name)
        }
    }

    private suspend fun fetchBooksByName(name: String): BooksUiStateSearch {
        return try {
            val result = booksRepository.getBooks(name).shuffled()
            Log.d("BookViewModel", result.toString())
            if (result.isNotEmpty()) {
                BooksUiStateSearch.Success(result)
            } else {
                BooksUiStateSearch.Error
            }

        } catch (e: IOException) {
            Log.e("BookViewModel", "Error IOException", e)
            BooksUiStateSearch.Error
        } catch (e: HttpException) {
            Log.e("BookViewModel", "Error HttpException", e)
            BooksUiStateSearch.Error
        }
    }

    fun booksApiLaunchCall() {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            booksUiState = try {
                coroutineScope {
                    val deferredResults = genres.map { genre ->
                        async { genre to booksRepository.getBooks(genre).shuffled() }
                    }

                    val results = deferredResults.awaitAll().toMap()

                    val horrorBooks = results["horror"].orEmpty()
                    val romanceBooks = results["romance"].orEmpty()
                    val mysteryBooks = results["mystery"].orEmpty()
                    val dystopianBooks = results["dystopian"].orEmpty()
                    val poetryBooks = results["poetry"].orEmpty()
                    val comicBooks = results["comic"].orEmpty()

                    val listOfTypes = listOf(
                        horrorBooks,
                        romanceBooks,
                        mysteryBooks,
                        dystopianBooks,
                        poetryBooks,
                        comicBooks
                    )

                    val typeNames =
                        genres.map { it.capitalize(Locale.ROOT) } // First letter capitalized

                    BooksUiState.Success(horrorBooks, romanceBooks, listOfTypes, typeNames)
                }
            } catch (e: IOException) {
                // Handle IOException
                Log.e("BookViewModel", "Error IOException: ${e.message}", e)
                BooksUiState.Error
            } catch (e: HttpException) {
                // Handle HttpException
                Log.e("BookViewModel", "Error HttpException: ${e.message}", e)
                BooksUiState.Error
            } catch (e: Exception) {
                // Handle any other exceptions
                Log.e("BookViewModel", "Unexpected error: ${e.message}", e)
                BooksUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory { //allows you to specify how the ViewModel should be initialized.
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                                as BookflixApplication)
                    val booksRepository = application.container.booksRepository
                    BookViewModel(booksRepository = booksRepository)
                }
            }
    }
}