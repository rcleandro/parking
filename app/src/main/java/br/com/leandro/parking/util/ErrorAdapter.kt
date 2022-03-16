package br.com.leandro.parking.util

class ErrorAdapter {
    companion object {
        fun adapt(error: String): String {
            error.let {
                return SignUpError.translate(it)
            }
        }
    }
}

enum class SignUpError(val internalErrorMessage: String, val message: String) {
    PLATE_IS_INVALID("is invalid", "Placa inválida"),
    NOT_FOUND("not found", "Não encontrado"),
    NOT_PAID("not paid", "Pagamento pendente"),
    MISSING_PLATE_PARAMETER("missing plate parameter", "Faltando parâmetro de placa"),
    ALREADY_PARKED("already parked", "Veículo já registrado");

    companion object {
        fun translate(internalErrorMessage: String): String {
            val errors = values()
            errors.forEach {
                if (internalErrorMessage.contains(it.internalErrorMessage, true)) {
                    return it.message
                }
            }
            return "Ocorreu um erro"
        }
    }
}