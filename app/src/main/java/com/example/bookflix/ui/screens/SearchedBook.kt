package com.example.bookflix.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookflix.R
import com.example.bookflix.model.Item
import com.example.bookflix.ui.BooksUiStateSearch

@Composable
fun SearchedBook(
    booksUiState: BooksUiStateSearch,
    onBookPressed: (Item) -> Unit,
) {
    when (booksUiState) {
        is BooksUiStateSearch.Loading -> LoadingScreenSearch()
        is BooksUiStateSearch.Success -> {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    .verticalScroll(scrollState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                booksUiState.searchedBooks
                    .forEach { thisBook ->
                        Column(
                            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(250.dp)
                                    .clickable(onClick = { onBookPressed(thisBook) }),
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(
                                        try {
                                            (thisBook.volumeInfo.imageLinks?.thumbnail)?.replace(
                                                "http",
                                                "https"
                                            )
                                        }catch(e : Exception){
                                            (thisBook.volumeInfo.imageLinks?.smallThumbnail)?.replace(
                                                "http",
                                                "https"
                                            )
                                        }
                                    )
                                    .crossfade(true)
                                    .build(),
                                contentDescription = thisBook.volumeInfo.title,
                                contentScale = ContentScale.Fit,
                                error = painterResource(id = R.drawable.ic_broken_image),
                                placeholder = painterResource(id = R.drawable.loading_img)
                            )
                            Text(text = thisBook.volumeInfo.title)
                        }
                    }
            }
        }

        else -> ErrorScreenSearch()
    }
}

@Composable
fun LoadingScreenSearch(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )

    }
}

@Composable
fun ErrorScreenSearch(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.noBooks), modifier = Modifier.padding(16.dp))
    }
}
