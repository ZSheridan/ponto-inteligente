package ai.gobots.sheridan.pontointeligente.api.controller

import ai.gobots.sheridan.pontointeligente.api.dto.CompanyDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.service.impl.CompanyServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/companies")
class CompanyController(private val companyServiceImpl: CompanyServiceImpl) {

    @GetMapping("/cnpj/{cnpj}")
    fun findByCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<CompanyDto>> =
            companyServiceImpl.getCompanyByCnpj(cnpj)

}