package com.example.bookflix.ui

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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            booksUiState = try {
                val deferredHorror = async { booksRepository.getBooks("horror").shuffled() }
                val deferredRomance = async { booksRepository.getBooks("romance").shuffled() }
                val deferredMystery = async { booksRepository.getBooks("mystery").shuffled() }
                val deferredDystopian = async { booksRepository.getBooks("dystopian").shuffled() }
                val deferredPoetry = async { booksRepository.getBooks("poetry").shuffled() }
                val deferredComic = async { booksRepository.getBooks("comic").shuffled() }

                val horrorBooks = deferredHorror.await()
                val romanceBooks = deferredRomance.await()
                val mysteryBooks = deferredMystery.await()
                val dystopianBooks = deferredDystopian.await()
                val poetryBooks = deferredPoetry.await()
                val comicBooks = deferredComic.await()

                val listOfTypes = listOf(
                    horrorBooks,
                    romanceBooks,
                    mysteryBooks,
                    dystopianBooks,
                    poetryBooks,
                    comicBooks
                )
                val typeNames = listOf(
                    "Horror",
                    "Romance",
                    "Mystery",
                    "Dystopian",
                    "Poetry",
                    "Comic"
                )
                BooksUiState.Success(horrorBooks, romanceBooks, listOfTypes, typeNames)
            } catch (e: IOException) {
                BooksUiState.Error
            } catch (e: HttpException) {
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