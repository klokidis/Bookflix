package com.example.bookflix.model

data class UiState(
    val bookSelected: Item = Item(
        kind = "",
        id = "",
        etag = "",
        selfLink = "",
        volumeInfo = volumeInfo(
            title = "",
            imageLinks = imageLinks(
                smallThumbnail = "",
                thumbnail = ""
            )
        ),
        saleInfo = SaleInfo(
            country = "",
            saleability = "",
            isEbook = false
        ),
        accessInfo = AccessInfo(
            country = "",
            viewability = "",
            embeddable = false,
            publicDomain = false,
            textToSpeechPermission = "",
            epub = Epub(isAvailable = false),
            pdf = Pdf(isAvailable = false),
            webReaderLink = "",
            accessViewStatus = "",
            quoteSharingAllowed = false
        )
    )
)
