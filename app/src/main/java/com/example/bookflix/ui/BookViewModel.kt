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

sealed interface BooksUiState {
    data class Success(
        val horrorBooks: List<Item>,
        val romanceBooks: List<Item>,
        val bookTypes: List<List<Item>>,
        val typeNames: List<String>
    ) : BooksUiState

    object Error : BooksUiState
    object Loading : BooksUiState
}

class BookViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    private val genres = listOf("horror", "romance", "mystery", "dystopian", "poetry", "comic")

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        booksApiCall()
    }

    fun selectBook(book: Item) {
        _uiState.update { currentState ->
            currentState.copy(
                bookSelected = book
            )
        }
    }

    fun booksApiCall() {
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

                    val typeNames = genres.map { it.capitalize(Locale.ROOT) } //first letter capitalized

                    BooksUiState.Success(horrorBooks, romanceBooks, listOfTypes, typeNames)
                }
            } catch (e: IOException) {
                // Handle IOException
                Log.e("BookViewModel", "Error IOException", e)
                BooksUiState.Error
            } catch (e: HttpException) {
                // Handle HttpException
                Log.e("BookViewModel", "Error HttpException", e)
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