package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.api.dto.EmployeeDto
import ai.gobots.sheridan.pontointeligente.api.dto.RegisterPFDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.repository.EmployeeRepository
import ai.gobots.sheridan.pontointeligente.domain.service.EmployeeService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

@Service
class EmployeeServiceImpl(private val employeeRepository: EmployeeRepository) : EmployeeService {

    override fun findByCpf(cpf: String): Employee? = employeeRepository.findByCpf(cpf)

    override fun findByEmail(email: String): Employee? = employeeRepository.findByEmail(email)

    override fun findById(id: String): Employee? = employeeRepository.findById(id).orElse(null)

    override fun persist(employee: Employee): Employee = employeeRepository.save(employee)

    fun employeeExists(id: String): Boolean = findById(id) != null

    fun registerPF(registerPFDto: RegisterPFDto, result: BindingResult, companyServiceImpl: CompanyServiceImpl)
            : ResponseEntity<Response<RegisterPFDto>> {

        val response = Response<RegisterPFDto>()

        validateInput(registerPFDto, result, companyServiceImpl)

        return if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage ?: "") }

            ResponseEntity.badRequest().body(response)
        } else {
            val company = companyServiceImpl.findByCnpj(registerPFDto.cnpj ?: "")
            val employee = persist(registerPFDto.toEmployee(company?.id ?: ""))
            response.data = company?.let { registerPFDto.createFrom(it, employee) }

            ResponseEntity.ok(response)
        }

    }

    fun updateEmployee(id: String, employeeDto: EmployeeDto, result: BindingResult)
            : ResponseEntity<Response<EmployeeDto>> {

        var newEmployeeDto: EmployeeDto

        if (findById(id) == null) {
            result.addError(ObjectError(
                    "Employee",
                    "Employee not found with id $id"
            ))
        }

        if (result.hasErrors()) return badRequest(result)

        with(employeeDto) {
            newEmployeeDto = EmployeeDto(name, email, password, hourWage, hoursWorked, hoursLunch, id)
        }
        return requestOkAndPersist(newEmployeeDto)

    }

    fun validateEmployee(id: String, result: BindingResult) {

        if (!employeeExists(id)) {
            result.addError(ObjectError(
                    "Entry",
                    "Employee not found with id $id"
            ))
        }

    }

    private fun badRequest(result: BindingResult): ResponseEntity<Response<EmployeeDto>> {

        val response = Response<EmployeeDto>()

        for (error in result.allErrors) {
            response.errors.add(error.defaultMessage ?: "")
        }

        return ResponseEntity.badRequest().body(response)

    }

    private fun requestOkAndPersist(employeeDto: EmployeeDto): ResponseEntity<Response<EmployeeDto>> {

        val response = Response<EmployeeDto>()

        var employee = employeeDto.id?.let { findById(it) }

        employee = employeeDto.updateEmployee(employee)

        response.data = persist(employee).toEmployeeDto()
        return ResponseEntity.ok(response)

    }

    private fun validateInput(registerPFDto: RegisterPFDto, result: BindingResult,
                              companyServiceImpl: CompanyServiceImpl) {

        val company = companyServiceImpl.findByCnpj(registerPFDto.cnpj ?: "")
        val employeeByCpf = findByCpf(registerPFDto.cpf ?: "")
        val employeeByEmail = findByEmail(registerPFDto.email ?: "")

        if (company == null)
            result.addError(ObjectError("Company",
                    "Company not registered with CNPJ: ${registerPFDto.cnpj}"))
        if (employeeByCpf != null)
            result.addError(ObjectError("Employee",
                    "Employee already exists with CPF: ${registerPFDto.cpf}"))
        if (employeeByEmail != null)
            result.addError(ObjectError("Employee",
                    "Email already in use: ${registerPFDto.email}"))
    }

}