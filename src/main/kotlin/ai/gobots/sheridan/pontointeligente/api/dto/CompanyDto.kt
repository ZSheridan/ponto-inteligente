package ai.gobots.sheridan.pontointeligente.api.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class CompanyDto (
        val companyName: String,
        val cnpj: String,
        val id: String? = null
)