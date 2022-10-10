package br.com.myself.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.myself.domain.Entrada
import br.com.myself.infrastructure.repository.EntradaRepository
import br.com.myself.presentation.util.Utils
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*

class EntradaFormViewModel(
    private val repository: EntradaRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _userMessage = MutableLiveData<String>()
    private val _events = MutableLiveData<Events>()
    private val _entrada = MutableLiveData<Entrada>(savedStateHandle["entrada"])

    val userMessage: LiveData<String> get() = _userMessage
    val events: LiveData<Events> get() = _events
    val entrada: LiveData<Entrada> get() = _entrada


    private var formDescricao: String = savedStateHandle["descricao"] ?: ""
    private var formValor: Double = savedStateHandle["valor"] ?: 0.0
    private var formData: Calendar = savedStateHandle["data"] ?: Utils.getCalendar()

    fun setDescricao(descricao: String) {
        this.formDescricao = descricao
        savedStateHandle["descricao"] = descricao
    }

    fun setValor(valor: Double) {
        this.formValor = valor
        savedStateHandle["valor"] = valor
    }

    fun setData(data: Calendar) {
        this.formData = data
        savedStateHandle["data"] = data
    }

    fun salvarEntrada() {
        if (validarForm()) {
            viewModelScope.launch {

                _events.value = Events.Saving
                val entrada = entrada.value?.copy(
                    valor = formValor,
                    descricao = formDescricao,
                    data = formData,
                ) ?: Entrada(
                    valor = formValor,
                    descricao = formDescricao,
                    data = formData,
                )

                repository.salvar(entrada)
                _userMessage.postValue("Dados salvos!")
                _events.value = Events.SavingSuccessful

            }
        }
    }

    private fun validarForm(): Boolean {
        return when {
            formDescricao.isBlank() -> {
                _userMessage.postValue("Campo Descricao Vazio")
                _events.value =
                    Events.NotSaving // Emitir NotSaving implicará no comportamento do layout, mesmo não sendo observado pelo fragment
                false
            }
            formValor.toBigDecimal() <= BigDecimal.ZERO -> {
                _userMessage.postValue("Campo Valor deve ser maior que zero")
                _events.value = Events.NotSaving
                false
            }
            else -> true
        }
    }

    sealed class Events {
        object SavingSuccessful : Events()
        object Saving : Events()
        object NotSaving : Events()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: EntradaRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return EntradaFormViewModel(repository, extras.createSavedStateHandle()) as T
        }
    }

}
