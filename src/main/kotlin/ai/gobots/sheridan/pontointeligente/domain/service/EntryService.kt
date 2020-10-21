package ai.gobots.sheridan.pontointeligente.domain.service

import ai.gobots.sheridan.pontointeligente.domain.document.Entry
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface EntryService {

    fun findByEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Entry>?

    fun findById(id: String): Entry?

    fun persist(entry: Entry): Entry

    fun remove(id: String)

}