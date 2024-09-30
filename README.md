# Bookflix

Bookflix is an Android application built using Kotlin and Jetpack Compose. The app allows users to search for books using the Google Books API. It employs modern Android development practices including Coroutines for asynchronous programming, Retrofit for network operations, and a repository pattern for data management.

## Features

- Search for books using the Google Books API.
- View details of selected books.
- Modern UI design with Jetpack Compose.
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

Upon launching Bookflix, you'll be greeted with a list of books fetched from the Google Books API. You can use the search feature to find books by title, author, or keyword. Tap on any book to view detailed information.

## Screenshots
<img src="https://github.com/user-attachments/assets/2e4b5a6b-6885-4cb7-b4cf-e424afee37b0" width="200" alt="Screenshot2">
<img src="https://github.com/user-attachments/assets/2508a16f-32c6-4d8c-afea-6c19cf54a2c8" width="200" alt="Screenshot3">
<img src="https://github.com/klokidis/Bookflix/assets/132920931/7e4a7f7f-21af-4f12-949b-5ccb7b26333d" width="200" alt="Screenshot1">
