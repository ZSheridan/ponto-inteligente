package ai.gobots.sheridan.pontointeligente.domain.service

import ai.gobots.sheridan.pontointeligente.domain.document.Employee

interface EmployeeService {

    fun findByCpf(cpf: String): Employee?

    fun findByEmail(email: String): Employee?

    fun findById(id: String): Employee?

    fun persist(employee: Employee): Employee

}