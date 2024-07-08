package com.example.bookflix.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookflix.R
import com.example.bookflix.model.Item
import com.example.bookflix.ui.BooksUiState

@Composable
fun HomeScreen(
    booksUiState: BooksUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (booksUiState) {
        is BooksUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BooksUiState.Success -> {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ){
                booksUiState.bookTypes.zip(booksUiState.typeNames).forEach { (books, typeName) ->
                    RowOfBooks(books, typeName)
                }
            }
        }
        else -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun RowOfBooks(
    photos: List<Item>,
    bookType: String
){
    Column(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(
            text = bookType,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 2.dp)
        )
        LazyRow(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            items(photos.size) { index ->
                val photo = photos[index]

                AsyncImage(
                    modifier = Modifier
                        .width(140.dp)
                        .padding(end = 5.dp)
                        .fillParentMaxHeight(),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data((photo.volumeInfo.imageLinks.thumbnail).replace("http", "https"))
                        .crossfade(true)
                        .build(),
                    contentDescription = photo.volumeInfo.title,
                    contentScale = ContentScale.FillBounds,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )

                Spacer(modifier = Modifier.width(5.dp))
            }

        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
