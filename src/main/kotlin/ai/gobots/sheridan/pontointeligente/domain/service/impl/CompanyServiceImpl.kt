package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.api.dto.CompanyDto
import ai.gobots.sheridan.pontointeligente.api.dto.RegisterPJDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.document.Company
import ai.gobots.sheridan.pontointeligente.domain.repository.CompanyRepository
import ai.gobots.sheridan.pontointeligente.domain.service.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

@Service
class CompanyServiceImpl(private val companyRepository: CompanyRepository) : CompanyService {

    override fun findByCnpj(cnpj: String): Company? = companyRepository.findByCnpj(cnpj)

    override fun persist(company: Company): Company = companyRepository.save(company)

    fun getCompanyByCnpj(cnpj: String): ResponseEntity<Response<CompanyDto>> {

        val company = findByCnpj(cnpj) ?: return badRequest(cnpj)

        return companyFound(company)

    }

    fun registerPJ(registerPJDto: RegisterPJDto, result: BindingResult, employeeServiceImpl: EmployeeServiceImpl)
            : ResponseEntity<Response<RegisterPJDto>> {

        val response = Response<RegisterPJDto>()

        validateInput(registerPJDto, result, employeeServiceImpl)

        return if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage ?: "") }

            ResponseEntity.badRequest().body(response)
        } else {
            val company = persist(registerPJDto.toCompany())
            val employee = employeeServiceImpl.persist(registerPJDto.toEmployee(company.id ?: ""))
            response.data = registerPJDto.createFrom(company, employee)

            ResponseEntity.ok(response)
        }

    }

    private fun badRequest(cnpj: String): ResponseEntity<Response<CompanyDto>> {

        val response = Response<CompanyDto>()

        response.errors.add("Company not found with CNPJ: $cnpj")

        return ResponseEntity.badRequest().body(response)

    }

    private fun companyFound(company: Company): ResponseEntity<Response<CompanyDto>> {

        val response = Response<CompanyDto>()

        response.data = CompanyDto(company.companyName, company.cnpj, company.id)

        return ResponseEntity.ok(response)

    }

    private fun validateInput(registerPJDto: RegisterPJDto, result: BindingResult,
                              employeeServiceImpl: EmployeeServiceImpl) {

        val company = findByCnpj(registerPJDto.cnpj ?: "")
        val employeeByCpf = employeeServiceImpl.findByCpf(registerPJDto.cpf ?: "")
        val employeeByEmail = employeeServiceImpl.findByEmail(registerPJDto.email ?: "")

        if (company != null)
            result.addError(ObjectError("Company",
                    "Company already exists with CNPJ: ${registerPJDto.cnpj}"))
        if (employeeByCpf != null)
            result.addError(ObjectError("Employee",
                    "Employee already exists with CPF: ${registerPJDto.cpf}"))
        if (employeeByEmail != null)
            result.addError(ObjectError("Employee",
                    "Email already in use: ${registerPJDto.email}"))
    }

}