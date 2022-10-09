package br.com.myself.presentation.adapter.state

import br.com.myself.domain.Entrada

sealed class UIModelState {
    //Entrada
    data class UIEntradaState(val entrada: Entrada) : UIModelState()
    data class SeparatorEntrada(val mes: Int) : UIModelState()

    // Registro
    data class UIRegistroState(
        val id: String,
        val descricao: String,
        val valor: String,
        val data: String,
        val outros: String?,

        val onItemSelected: () -> Unit
    ): UIModelState()
}