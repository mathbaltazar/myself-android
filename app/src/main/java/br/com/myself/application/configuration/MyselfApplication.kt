package br.com.myself.application.configuration

import android.app.Application
import br.com.myself.infrastructure.database.LocalDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyselfApplication : Application() {

    val database: LocalDatabase by lazy { LocalDatabase.getInstance(this) }

}