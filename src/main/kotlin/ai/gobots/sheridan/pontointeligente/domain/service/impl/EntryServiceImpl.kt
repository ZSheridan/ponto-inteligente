package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.api.dto.EntryDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.document.Entry
import ai.gobots.sheridan.pontointeligente.domain.repository.EntryRepository
import ai.gobots.sheridan.pontointeligente.domain.service.EntryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

@Service
class EntryServiceImpl(private val entryRepository: EntryRepository) : EntryService {

    override fun findByEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Entry>? =
            entryRepository.findByEmployeeId(employeeId, pageRequest)

    override fun findById(id: String): Entry? = entryRepository.findById(id).orElse(null)

    override fun persist(entry: Entry): Entry = entryRepository.save(entry)

    override fun remove(id: String) = entryRepository.deleteById(id)

    fun deleteById(id: String): ResponseEntity<Response<Unit>> {

        return if (entryExists(id)) {
            remove(id)
            ResponseEntity.noContent().build()
        } else entryNotFound(id)

    }

    fun getEntriesByEmployeeId(employeeId: String, page: Int, quantityPerPage: Int, dir: String, ord: String)
            : ResponseEntity<Response<Page<EntryDto>>> {

        val response = Response<Page<EntryDto>>()
        val pageRequest = PageRequest.of(page, quantityPerPage, Sort.Direction.valueOf(dir), ord)
        val entries: Page<Entry>? = findByEmployeeId(employeeId, pageRequest)
        val entriesDto: Page<EntryDto>? = entries?.map { entry -> entry.toEntryDto() }

        response.data = entriesDto
        return ResponseEntity.ok(response)

    }

    fun updateEntry(id: String, entryDto: EntryDto, result: BindingResult): ResponseEntity<Response<EntryDto>> {

        var newEntryDto: EntryDto

        if (findById(id) == null) {
            result.addError(ObjectError(
                    "Entry",
                    "Entry not found with id $id"
            ))
        }

        if (result.hasErrors()) return badRequest(result)

        with(entryDto) {
            newEntryDto = EntryDto(date, type, employeeId, description, location, id)
        }
        return requestOkAndPersist(newEntryDto)

    }

    fun validateAndAddEntry(entryDto: EntryDto, result: BindingResult, employeeServiceImpl: EmployeeServiceImpl)
            : ResponseEntity<Response<EntryDto>> {

        if (!result.hasErrors())
            entryDto.employeeId?.let { id -> employeeServiceImpl.validateEmployee(id, result) }

        return if (result.hasErrors()) badRequest(result) else requestOkAndPersist(entryDto)

    }

    fun validateAndGetEntryById(id: String): ResponseEntity<Response<EntryDto>> {

        val response = Response<EntryDto>()
        val entry: Entry? = findById(id)

        return if (entry == null) {
            entryNotFound(id)
        } else {
            response.data = entry.toEntryDto()
            ResponseEntity.ok(response)
        }

    }

    private fun badRequest(result: BindingResult): ResponseEntity<Response<EntryDto>> {

        val response = Response<EntryDto>()

        for (error in result.allErrors) {
            response.errors.add(error.defaultMessage ?: "")
        }
        return ResponseEntity.badRequest().body(response)

    }

    private fun entryExists(id: String) = findById(id) != null

    private fun <T> entryNotFound(id: String): ResponseEntity<Response<T>> {

        val response = Response<T>()

        response.errors.add("Entry not found with id $id")
        return ResponseEntity.badRequest().body(response)

    }

    private fun requestOkAndPersist(entryDto: EntryDto): ResponseEntity<Response<EntryDto>> {

        val response = Response<EntryDto>()

        response.data = persist(entryDto.toEntry()).toEntryDto()
        return ResponseEntity.ok(response)

    }

}