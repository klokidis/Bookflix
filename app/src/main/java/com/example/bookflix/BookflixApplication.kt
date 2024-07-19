package com.example.bookflix

import android.app.Application
import com.example.bookflix.data.AppContainer
import com.example.bookflix.data.DefaultAppContainer

class BookflixApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()//This container holds all the dependencies (like databases, network clients, or repositories) that the app needs.
    }
}