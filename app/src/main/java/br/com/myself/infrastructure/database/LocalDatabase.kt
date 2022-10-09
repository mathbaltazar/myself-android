package br.com.myself.infrastructure.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.myself.BuildConfig
import br.com.myself.domain.Crise
import br.com.myself.domain.Entrada
import br.com.myself.domain.Registro
import br.com.myself.infrastructure.dao.CriseDAO
import br.com.myself.infrastructure.dao.EntradaDAO
import br.com.myself.infrastructure.dao.RegistroDAO
import br.com.myself.infrastructure.database.typeconverters.DateConverter

@Database(entities = [
    Registro::class,
    Entrada::class,
    Crise::class
],
version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    
    abstract fun getRegistroDAO(): RegistroDAO
    
    abstract fun getEntradaDAO(): EntradaDAO
    
    abstract fun getCriseDAO(): CriseDAO
    
    companion object {
        private const val NAME = BuildConfig.DATABASE_NAME
        var instance: LocalDatabase? = null
        fun getInstance(context: Context) = synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                NAME
            ).fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}