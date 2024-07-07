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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BooksUiState {
    data class Success(val horrorBooks: List<Item>, val romanceBooks: List<Item>) : BooksUiState
    object Error : BooksUiState
    object Loading : BooksUiState
}
class BookViewModel (private val booksRepository: BooksRepository) : ViewModel(){
    var booksUiState : BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            booksUiState = try {
                val horrorBooks = booksRepository.getBooks("horror")
                val romanceBooks = booksRepository.getBooks("romance")
                BooksUiState.Success(horrorBooks, romanceBooks)
            } catch (e: IOException) {
                BooksUiState.Error
            } catch (e: HttpException) {
                BooksUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory { //allows you to specify how the ViewModel should be initialized.
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BookflixApplication)
                val booksRepository = application.container.booksRepository
                BookViewModel(booksRepository = booksRepository)
            }
        }
    }
}