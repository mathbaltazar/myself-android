package br.com.myself.infrastructure.repository

import androidx.lifecycle.LiveData
import br.com.myself.domain.Crise
import br.com.myself.infrastructure.dao.CriseDAO

class CriseRepository(private val dao: CriseDAO) {

    fun salvar(crise: Crise): Long {
        return dao.persist(crise)
    }

    fun excluir(crise: Crise) {
        dao.delete(crise)
    }

    fun getTodasCrises(): LiveData<List<Crise>> {
        return dao.findAll()
    }

}
