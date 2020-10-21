package ai.gobots.sheridan.pontointeligente.domain.service

import ai.gobots.sheridan.pontointeligente.domain.document.Company

interface CompanyService {

    fun findByCnpj(cnpj: String): Company?

    fun persist(company: Company): Company

}