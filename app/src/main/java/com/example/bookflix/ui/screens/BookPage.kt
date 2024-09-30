package com.example.bookflix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun BookPage(
    book: Item
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .height(290.dp)
                .padding(end = 6.dp),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data((book.volumeInfo.imageLinks?.thumbnail)?.replace("http", "https"))
                .crossfade(true)
                .build(),
            contentDescription = book.volumeInfo.title,
            contentScale = ContentScale.Fit,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        RowOfText(stringResource(id = R.string.title), book.volumeInfo.title)
        Spacer(modifier = Modifier.padding(10.dp))
        if (book.volumeInfo.language != null) {
            RowOfText(
                stringResource(id = R.string.languae),
                book.volumeInfo.language.replace("en", "English")
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        if (book.volumeInfo.description != null) {
            RowOfText(stringResource(id = R.string.description), book.volumeInfo.description)
        }
    }
}

@Composable
fun RowOfText(contains: String, mainText: String) {
    Row {
        Text(
            text = contains
        )
        Text(
            text = mainText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}