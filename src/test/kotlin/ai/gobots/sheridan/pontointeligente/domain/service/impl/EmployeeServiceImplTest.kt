package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum.ROLE_ADMIN
import ai.gobots.sheridan.pontointeligente.domain.repository.EmployeeRepository
import ai.gobots.sheridan.pontointeligente.domain.utils.PasswordUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
internal class EmployeeServiceImplTest {

    @Autowired
    private lateinit var employeeServiceImpl: EmployeeServiceImpl

    @MockBean
    private lateinit var employeeRepository: EmployeeRepository

    private val cpf = "123456789-00"
    private val email = "bogots@gobots.ai"
    private val id = "E1"
    private val employee: Employee = Employee("GoBots", email, cpf, PasswordUtils().generateBcrypt("123"), ROLE_ADMIN, "")

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
                .given(employeeRepository.save(any(Employee::class.java)))
                .willReturn(employee)
        BDDMockito
                .given(employeeRepository.findByCpf(anyString()))
                .willReturn(employee)
        BDDMockito
                .given(employeeRepository.findByEmail(anyString()))
                .willReturn(employee)
        BDDMockito
                .given(employeeRepository.findById(anyString()))
                .willReturn(Optional.of(employee))
    }

    @Test
    fun `should save and return an employee`() {
        val newEmployee = employeeServiceImpl.persist(employee)
        assertNotNull(newEmployee)
        verify(employeeRepository, times(1)).save(employee)
    }

    @Test
    fun `should return an employee by its CPF`() {
        val newEmployee = employeeServiceImpl.findByCpf(cpf)
        assertEquals(employee, newEmployee)
        verify(employeeRepository, times(1)).findByCpf(cpf)
    }

    @Test
    fun `should return and employee by its email`() {
        val newEmployee = employeeServiceImpl.findByEmail(email)
        assertEquals(employee, newEmployee)
        verify(employeeRepository, times(1)).findByEmail(email)
    }

    @Test
    fun `should return and employee by its id`() {
        val newEmployee = employeeServiceImpl.findById(id)
        assertEquals(employee, newEmployee)
        verify(employeeRepository, times(1)).findById(id)
    }

    @Test
    fun employeeExists() {
        assertTrue(employeeServiceImpl.employeeExists(id))
        verify(employeeRepository, times(1)).findById(id)
    }

}