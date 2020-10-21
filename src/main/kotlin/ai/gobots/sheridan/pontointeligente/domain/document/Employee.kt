package ai.gobots.sheridan.pontointeligente.domain.document

import ai.gobots.sheridan.pontointeligente.api.dto.EmployeeDto
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Employee(
        val name: String,
        val email: String,
        val cpf: String,
        val password: String,
        val profile: ProfileEnum,
        val companyId: String,
        val hourWage: Double = 0.0,
        val hoursWorked: Float = 0.0f,
        val hoursLunch: Float = 0.0f,

        @Id val id: String? = null
) {

    fun toEmployeeDto() = EmployeeDto(name, email, null, hourWage.toString(), hoursWorked.toString(),
            hoursLunch.toString(), id)

}