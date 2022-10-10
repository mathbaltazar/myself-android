package br.com.myself.presentation.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.com.myself.application.configuration.MyselfApplication
import br.com.myself.domain.Crise
import br.com.myself.infrastructure.repository.CriseRepository

class CrisesActivityViewModel(application: MyselfApplication): AndroidViewModel(application) {
    private val repository by lazy { CriseRepository(application.database.getCriseDAO()) }
    
    val crises: LiveData<List<Crise>> = repository.getTodasCrises()
    
    fun excluirCrise(crise: Crise, onDeleted: () -> Unit) {
        // TODO migrar suspend functions
    }

    fun salvarCrise(novacrise: Crise, onSaved: () -> Unit) {
        // TODO migrar suspend functions
    }
    
}
