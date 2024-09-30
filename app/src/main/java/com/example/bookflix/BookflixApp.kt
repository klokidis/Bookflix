package com.example.bookflix

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookflix.ui.BookViewModel
import com.example.bookflix.ui.screens.BookPage
import com.example.bookflix.ui.screens.HomeScreen
import com.example.bookflix.ui.screens.SearchedBook

enum class Screens {
    Start,
    BookPage,
    Searched
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookflixApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Start.name
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (navController.previousBackStackEntry != null) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.previous_button)
                            )
                        }
                    },
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                )
            }
        }
    ) { innerPadding ->

        val bookViewModel: BookViewModel = viewModel(factory = BookViewModel.Factory)
        val uiState = bookViewModel.booksUiState
        val uiStateSearch = bookViewModel.booksUiStateSearch
        val uiStateValues = bookViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) },
            ) {
            composable(route = Screens.Start.name) {
                HomeScreen(
                    retryAction = { bookViewModel.booksApiLaunchCall() },
                    onBookPressed = {
                        bookViewModel.selectBook(it)
                        navController.navigate(Screens.BookPage.name)
                    },
                    onSearched = {
                        bookViewModel.searchBook(it)
                        navController.navigate(Screens.Searched.name)
                    },
                    booksUiState = uiState,
                )
            }
            composable(route = Screens.BookPage.name) {
                BookPage(
                    uiStateValues.value.bookSelected
                )
            }
            composable(route = Screens.Searched.name) {
                SearchedBook(
                    booksUiState = uiStateSearch,
                    onBookPressed = {
                        bookViewModel.selectBook(it)
                        navController.navigate(Screens.BookPage.name)
                    }
                )
            }
        }
    }
}
