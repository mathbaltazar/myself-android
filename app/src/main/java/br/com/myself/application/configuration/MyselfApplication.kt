package br.com.myself.application.configuration

import android.app.Application
import br.com.myself.infrastructure.database.LocalDatabase

class MyselfApplication : Application() {

    val database: LocalDatabase by lazy { LocalDatabase.getInstance(this) }

}