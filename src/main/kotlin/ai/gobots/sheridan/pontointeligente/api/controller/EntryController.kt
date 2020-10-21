package ai.gobots.sheridan.pontointeligente.api.controller

import ai.gobots.sheridan.pontointeligente.api.dto.EntryDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EmployeeServiceImpl
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EntryServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/entries")
class EntryController(private val entryServiceImpl: EntryServiceImpl,
                      private val employeeServiceImpl: EmployeeServiceImpl) {

    @Value("\${pagination.quantity-per-page}")
    private val quantityPerPage = 0

    @PostMapping
    fun add(@Valid @RequestBody entryDto: EntryDto, result: BindingResult): ResponseEntity<Response<EntryDto>> =
            entryServiceImpl.validateAndAddEntry(entryDto, result, employeeServiceImpl)

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: String): ResponseEntity<Response<EntryDto>> =
            entryServiceImpl.validateAndGetEntryById(id)

    @GetMapping("/employee/{employeeId}")
    fun listByEmployeeId(
            @PathVariable(value = "employeeId") employeeId: String,
            @RequestParam(value = "page", defaultValue = "0") page: Int,
            @RequestParam(value = "ord", defaultValue = "id") ord: String,
            @RequestParam(value = "dir", defaultValue = "DESC") dir: String
    ): ResponseEntity<Response<Page<EntryDto>>> {

        val response = Response<Page<EntryDto>>()

        return if (!employeeServiceImpl.employeeExists(employeeId)) {
            response.errors.add("Employee not found.")
            ResponseEntity.badRequest().body(response)
        } else {
            entryServiceImpl.getEntriesByEmployeeId(employeeId, page, quantityPerPage, dir, ord)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String, @Valid @RequestBody entryDto: EntryDto,
               result: BindingResult): ResponseEntity<Response<EntryDto>> {

        if (!result.hasErrors())
            entryDto.employeeId?.let { employeeServiceImpl.validateEmployee(it, result) }

        return entryServiceImpl.updateEntry(id, entryDto, result)

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Response<Unit>> =
        entryServiceImpl.deleteById(id)

}