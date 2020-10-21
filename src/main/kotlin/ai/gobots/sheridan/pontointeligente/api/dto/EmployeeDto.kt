package ai.gobots.sheridan.pontointeligente.api.dto

import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum.ROLE_USER
import ai.gobots.sheridan.pontointeligente.domain.utils.PasswordUtils
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EmployeeDto (
        @get:NotEmpty(message = "Name must not be empty.")
        @get:Size(min = 2, max = 100, message = "Name must have between 2 and 100 characters.")
        val name: String,

        @get:NotEmpty(message = "Email must not be empty.")
        @get:Size(min = 5, max = 200, message = "Email must have between 5 and 200 characters.")
        @get:Email(message = "Insert a valid email.")
        val email: String,

        val password: String? = null,
        val hourWage: String? = null,
        val hoursWorked: String? = null,
        val hoursLunch: String? = null,
        val id: String? = null
) {

    fun updateEmployee(employee: Employee? = null) = Employee(name, email, employee?.cpf ?: "",
            PasswordUtils().generateBcrypt(password ?: ""), employee?.profile ?: ROLE_USER, employee?.companyId ?: "",
            hourWage?.toDouble() ?: 0.0, hoursWorked?.toFloat() ?: 0.0f,
            hoursLunch?.toFloat() ?: 0.0f, employee?.id)

}