package kz.validol.hacknu

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import kz.validol.hacknu.auth.authModule
import kz.validol.hacknu.core.coreModule
import kz.validol.hacknu.local_storage.AppLocalDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModules: List<Module>
    get() = listOf(
            authModule,
            coreModule,
            singletons
    )

val singletons = module {
    single { createSharedPrefs(androidContext()) }
}


internal fun createSharedPrefs(context: Context) : SharedPreferences {
    return context.applicationContext.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE)
}


fun createLocalStorage(context:Context) : AppLocalDatabase {
    return Room.databaseBuilder(context, AppLocalDatabase::class.java,"hacknu").build()
}