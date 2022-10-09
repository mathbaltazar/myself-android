package br.com.myself.presentation.ui.financas.state

data class RegistrosFragmentBehaviorState(
    val registroId: String?
) {
    companion object {
        fun create(): RegistrosFragmentBehaviorState {
            return RegistrosFragmentBehaviorState(
                registroId = null
            )
        }
    }
}