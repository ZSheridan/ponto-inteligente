package ai.gobots.sheridan.pontointeligente.api.controller

import ai.gobots.sheridan.pontointeligente.api.dto.RegisterPFDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.service.impl.CompanyServiceImpl
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EmployeeServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/register/pf")
class RegisterPFController(private val companyServiceImpl: CompanyServiceImpl,
                           private val employeeServiceImpl: EmployeeServiceImpl) {

    @PostMapping
    fun create(@Valid @RequestBody registerPFDto: RegisterPFDto, result: BindingResult)
            : ResponseEntity<Response<RegisterPFDto>> =
            employeeServiceImpl.registerPF(registerPFDto, result, companyServiceImpl)

}