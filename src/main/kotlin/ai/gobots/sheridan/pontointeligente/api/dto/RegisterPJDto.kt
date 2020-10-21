package ai.gobots.sheridan.pontointeligente.api.dto

import ai.gobots.sheridan.pontointeligente.domain.document.Company
import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum.ROLE_ADMIN
import ai.gobots.sheridan.pontointeligente.domain.utils.PasswordUtils
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegisterPJDto (
        @get:NotEmpty(message = "Name must not be empty.")
        @get:Size(min = 2, max = 100, message = "Name must have between 2 and 100 characters.")
        val name: String? = "",

        @get:NotEmpty(message = "Email must not be empty.")
        @get:Size(min = 5, max = 200, message = "Email must have between 5 and 200 characters.")
        @get:Email(message = "Insert a valid email.")
        val email: String?,

        @get:NotEmpty(message = "Password must not be empty.")
        val password: String?,

        @get:NotEmpty(message = "CPF must not be empty.")
        val cpf: String?,

        @get:NotEmpty(message = "CNPJ must not be empty.")
        val cnpj: String?,

        @get:NotEmpty(message = "Company name must not be empty.")
        @get:Size(min = 1, max = 200, message = "Company name must have between 1 and 200 characters.")
        val companyName: String?,

        val id: String? = null
) {

    fun createFrom(company: Company, employee: Employee) =
            RegisterPJDto(employee.name, employee.email, null, employee.cpf, company.cnpj,
                    company.companyName, employee.id ?: "")

    fun toCompany() = Company(companyName ?: "", cnpj ?: "")

    fun toEmployee(companyId: String) =
            Employee(name ?: "", email ?: "", cpf ?: "",
                    PasswordUtils().generateBcrypt(password ?: ""), ROLE_ADMIN, companyId)

}