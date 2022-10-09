package br.com.myself.infrastructure.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import br.com.myself.domain.Entrada
import br.com.myself.infrastructure.dao.EntradaDAO
import br.com.myself.presentation.util.DEFAULT_REQUEST_LOAD_SIZE

class EntradaRepository(
    private val dao: EntradaDAO
) {


    fun pesquisarEntradas(ano: Int): LiveData<PagingData<Entrada>> {
        val pagingSourceFactory = { dao.findAllByYear("$ano%") }

        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_REQUEST_LOAD_SIZE,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    suspend fun salvar(entrada: Entrada) {
        dao.persist(entrada)
        Log.d("EntradaRepository | salvar(Entrada)", "Entrada salva! --> $entrada")
    }

    suspend fun delete(entrada: Entrada) {
        dao.delete(entrada)
        Log.d("EntradaRepository | delete(Entrada)", "Entrada removida! --> $entrada")
    }

    fun count(ano: Int): LiveData<Int> {
        return dao.countByYear("$ano%")
    }

    fun getEntradaById(itemId: Long): LiveData<Entrada> {
        return dao.findById(itemId)
    }
}
