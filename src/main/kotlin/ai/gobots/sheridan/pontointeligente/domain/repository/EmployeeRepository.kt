package ai.gobots.sheridan.pontointeligente.domain.repository

import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import org.springframework.data.mongodb.repository.MongoRepository

interface EmployeeRepository : MongoRepository<Employee, String> {

    fun findByEmail(email: String): Employee?

    fun findByCpf(cpf: String): Employee?

    //fun findByIdOrNull(id: String): Employee?

}