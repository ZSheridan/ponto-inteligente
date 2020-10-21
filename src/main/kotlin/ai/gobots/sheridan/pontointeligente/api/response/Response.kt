package ai.gobots.sheridan.pontointeligente.api.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Response<T>(
        val errors: ArrayList<String> = arrayListOf(),
        var data: T? = null
)