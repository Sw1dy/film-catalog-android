package com.example.film_catalog_android

import android.app.Application
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.local.ThemeStorage
import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.di.appModule
import com.example.film_catalog_android.di.repositoryModule
import com.example.film_catalog_android.di.useCaseModule
import com.example.film_catalog_android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FilmCatalogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        UserSessionStorage.init(applicationContext)
        ThemeStorage.init(applicationContext)
        DatabaseProvider.init(applicationContext)

        startKoin {
            androidContext(this@FilmCatalogApplication)
            modules(
                appModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
