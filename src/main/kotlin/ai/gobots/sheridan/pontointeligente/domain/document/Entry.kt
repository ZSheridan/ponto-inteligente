package ai.gobots.sheridan.pontointeligente.domain.document

import ai.gobots.sheridan.pontointeligente.api.dto.EntryDto
import ai.gobots.sheridan.pontointeligente.domain.enum.TypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.text.SimpleDateFormat
import java.util.*

@Document
data class Entry(
        val date: Date,
        val type: TypeEnum,
        val employeeId: String,
        val description: String? = null,
        val location: String? = null,

        @Id val id: String? = null
) {

    fun toEntryDto() = EntryDto(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), type.toString(),
            employeeId, description, location, id)

}