package ai.gobots.sheridan.pontointeligente.domain.repository

import ai.gobots.sheridan.pontointeligente.domain.document.Company
import org.springframework.data.mongodb.repository.MongoRepository

interface CompanyRepository : MongoRepository<Company, String> {

    fun findByCnpj(cnpj: String): Company?

}