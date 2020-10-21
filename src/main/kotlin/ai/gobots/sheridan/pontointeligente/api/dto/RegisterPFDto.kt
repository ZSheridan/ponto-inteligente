package ai.gobots.sheridan.pontointeligente.api.dto

import ai.gobots.sheridan.pontointeligente.domain.document.Company
import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum.ROLE_USER
import ai.gobots.sheridan.pontointeligente.domain.utils.PasswordUtils
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RegisterPFDto (
        @get:NotEmpty(message = "Name must not be empty.")
        @get:Size(min = 2, max = 100, message = "Name must have between 2 and 100 characters.")
        val name: String?,

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

        val companyId: String = "",

        val hourWage: String? = null,
        val hoursWorked: String? = null,
        val hoursLunch: String? = null,
        val id: String? = null
) {

        fun createFrom(company: Company, employee: Employee) =
                RegisterPFDto(employee.name, employee.email, null, employee.cpf, company.cnpj,
                        company.id ?: "", employee.hourWage.toString(), employee.hoursWorked.toString(),
                        employee.hoursLunch.toString(), employee.id)

        fun toEmployee(companyId: String) =
                Employee(name ?: "", email ?: "", cpf ?: "",
                        PasswordUtils().generateBcrypt(password ?: ""), ROLE_USER, companyId)

}