package ai.gobots.sheridan.pontointeligente.api.controller

import ai.gobots.sheridan.pontointeligente.api.dto.EmployeeDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EmployeeServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/employees")
class EmployeeController(private val employeeServiceImpl: EmployeeServiceImpl) {

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @Valid @RequestBody employeeDto: EmployeeDto, result: BindingResult)
            : ResponseEntity<Response<EmployeeDto>> =
            employeeServiceImpl.updateEmployee(id, employeeDto, result)

}