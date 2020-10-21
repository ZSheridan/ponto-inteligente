package ai.gobots.sheridan.pontointeligente.security

import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EmployeeServiceImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmployeeDetailsService(val employeeServiceImpl: EmployeeServiceImpl) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            val employee: Employee? = employeeServiceImpl.findByEmail(username)
            if (employee != null) {
                return MainEmployee(employee)
            }
        }
        throw UsernameNotFoundException(username)
    }

}