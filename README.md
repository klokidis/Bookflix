# Bookflix

Bookflix is an Android application developed in Kotlin and Jetpack Compose. It leverages the Google Books API to display a collection of books, allowing users to view details by selecting individual books.

## Features

- Browse a collection of books fetched from the Google Books API.
- View detailed information about each selected book.
- Utilizes modern Android development tools and practices:
  - Coroutines for efficient and asynchronous programming.
  - Retrofit for seamless network operations.
  - Repository pattern for clean and structured data management.

## Architecture

Bookflix adheres to the MVVM (Model-View-ViewModel) architecture, ensuring separation of concerns and scalability. Key architectural components include:

- **ViewModel**: Manages UI-related data and business logic, keeping the UI codebase clean.
- **Repository**: Acts as a single source of truth for data, abstracting data sources and providing a clean API.
- **Retrofit**: Handles network requests to interact with the Google Books API.
- **Coroutines**: Facilitates non-blocking asynchronous programming, ensuring smooth data operations.
- **Jetpack Navigation Compose**: Facilitates navigation within the app, enhancing user experience and flow.

## Usage

Upon launching Bookflix, you'll be greeted with a list of books fetched from the Google Books API. Tap on any book to view detailed information. 

## Screenshots

(Screenshots to be added soon)
