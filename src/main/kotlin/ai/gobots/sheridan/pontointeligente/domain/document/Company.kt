package ai.gobots.sheridan.pontointeligente.domain.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Company(
        val companyName: String,
        val cnpj: String,

        @Id val id: String? = null
)