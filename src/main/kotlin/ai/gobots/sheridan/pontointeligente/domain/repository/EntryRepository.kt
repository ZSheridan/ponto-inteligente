package ai.gobots.sheridan.pontointeligente.domain.repository

import ai.gobots.sheridan.pontointeligente.domain.document.Entry
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface EntryRepository : MongoRepository<Entry, String> {

    fun findByEmployeeId(employeeId: String, pageable: Pageable): Page<Entry>

    //fun findByIdOrNull(id: String): Entry?

}