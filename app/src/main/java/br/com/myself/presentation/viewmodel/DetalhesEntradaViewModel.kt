package br.com.myself.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.myself.domain.Entrada
import br.com.myself.infrastructure.repository.EntradaRepository
import kotlinx.coroutines.launch

class DetalhesEntradaViewModel(
    private val repository: EntradaRepository,
    state: SavedStateHandle, // Captura os argumentos do fragment (definidos no navigation.xml)
) : ViewModel() {

    private val _eventStream = MutableLiveData<Events>()
    val eventStreamLiveData: LiveData<Events> get() = _eventStream

    val selectedEntrada: LiveData<Entrada> =
        repository.getEntradaById(state["entradaId"]!!)


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun excluirEFechar() = viewModelScope.launch {
        toggleLoading(true)

        repository.delete(selectedEntrada.value!!)
        toggleLoading(false)
        _eventStream.postValue(Events.CloseDetails)
    }

    private fun toggleLoading(loading: Boolean) {
        _isLoading.postValue(loading)
    }

    fun onEditarClick() {
        _eventStream.postValue(Events.Edit(selectedEntrada.value!!))
    }

    fun onDeleteClick() {
        _eventStream.postValue(Events.Delete(selectedEntrada.value!!))
    }

    sealed class Events {
        object CloseDetails : Events()
        class Edit(val entrada: Entrada) : Events()
        class Delete(val entrada: Entrada) : Events()
        class Message(val text: String?) : Events()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: EntradaRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return DetalhesEntradaViewModel(repository, extras.createSavedStateHandle()) as T
        }
    }
}
