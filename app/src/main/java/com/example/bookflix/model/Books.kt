package com.example.bookflix.model

import kotlinx.serialization.Serializable

@Serializable
data class BooksApiResponse(
    val kind: String,
    val totalItems: Long,
    val items: List<Item> = emptyList()
)
@Serializable
data class Item(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: volumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo,
    val searchInfo: SearchInfo? = null //nullable as it might not always be present
)
@Serializable
data class volumeInfo(
    val title: String,
    val subtitle: String? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null,
    val pageCount: Long? = null,
    val printedPageCount: Long? = null,
    val dimensions: Dimensions? = null,
    val printType: String? = null,
    val categories: List<String>? = null,
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val panelizationSummary: PanelizationSummary? = null,
    val imageLinks: imageLinks? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
    // Add other properties as needed
)
@Serializable
data class Dimensions(
    val height: String,
    val width: String,
    val thickness: String
)


@Serializable
data class IndustryIdentifier(
    val type: String,
    val identifier: String,
)
@Serializable
data class ReadingModes(
    val text: Boolean,
    val image: Boolean,
)
@Serializable
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean,
)
@Serializable
data class imageLinks(
    val smallThumbnail: String,
    val thumbnail: String,
)
@Serializable
data class SaleInfo(
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val listPrice: Price? = null,
    val retailPrice: Price? = null,
    val buyLink: String? = null,
    val offers: List<Offer>? = null
)

@Serializable
data class Offer(
    val finskyOfferType: Int,
    val listPrice: Price,
    val retailPrice: Price,
    val giftable: Boolean? = null
)
@Serializable
data class Price(
    val amount: Double? = null,
    val currencyCode: String? = null,
    val amountInMicros: Long? = null
)
@Serializable
data class AccessInfo(
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String,
    val epub: Epub,
    val pdf: Pdf,
    val webReaderLink: String,
    val accessViewStatus: String,
    val quoteSharingAllowed: Boolean,
)
@Serializable
data class Epub(
    val isAvailable: Boolean,
    val acsTokenLink: String? = null,
    val downloadLink: String? = null
)

@Serializable
data class Pdf(
    val isAvailable: Boolean,
    val acsTokenLink: String? = null,
    val downloadLink: String? = null
)
@Serializable
data class SearchInfo(
    val textSnippet: String,
)
