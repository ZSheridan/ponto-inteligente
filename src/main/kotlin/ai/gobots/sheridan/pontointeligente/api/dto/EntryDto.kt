package ai.gobots.sheridan.pontointeligente.api.dto

import ai.gobots.sheridan.pontointeligente.domain.document.Entry
import ai.gobots.sheridan.pontointeligente.domain.enum.TypeEnum
import com.fasterxml.jackson.annotation.JsonInclude
import java.text.SimpleDateFormat
import javax.validation.constraints.NotEmpty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EntryDto(

        /*The validated parameters must be NULLABLE for the bad request response to be well formed.
          Even if in reality they must not be null.
          Without it the bad request response won't specify when this parameter is absent.
         */
        @get:NotEmpty(message = "Date must not be empty.")
        val date: String?,

        @get:NotEmpty(message = "Type must not be empty.")
        val type: String?,

        @get:NotEmpty(message = "Employee ID must not be empty.")
        val employeeId: String?,

        val description: String? = null,
        val location: String? = null,
        val id: String? = null
) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val ex = Exception("Cannot proceed because of a null parameter.")

    fun toEntry(): Entry =
            Entry(dateFormat.parse(date) ?: throw ex, TypeEnum.valueOf(type ?: throw ex), employeeId ?: throw ex,
                    description, location, id)

}