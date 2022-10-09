package br.com.myself.infrastructure.repository

import androidx.lifecycle.LiveData
import br.com.myself.domain.Registro
import br.com.myself.infrastructure.dao.RegistroDAO

class RegistroRepository(private val registroDAO: RegistroDAO) {

    fun pesquisarRegistros(mes: Int, ano: Int): LiveData<List<Registro>> {
        // Seguindo o pattern "yyyy-MM-dd"
        var monthLike = "%-"
        if (mes < 10) monthLike += "0"
        monthLike += "${mes}-%"

        val yearLike = "$ano-%"

        return registroDAO.findAllRegistrosByData(monthLike, yearLike)
    }

    suspend fun salvarRegistro(registro: Registro) {
        registroDAO.persist(registro)
    }

    suspend fun excluirRegistro(registro: Registro) {
        registroDAO.delete(registro)
    }

    fun pesquisarRegistros(pesquisa: String): LiveData<List<Registro>> {
        return registroDAO.findAllRegistrosByDescricao("%$pesquisa%")
    }

    fun getRegistroById(registroId: Long): LiveData<Registro> {
        return registroDAO.findById(registroId)
    }

}