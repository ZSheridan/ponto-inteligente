package ai.gobots.sheridan.pontointeligente.domain.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
internal class PasswordUtilsTest {

    private val password = "123456789"
    private val bCryptEncoder = BCryptPasswordEncoder()

    @Test
    fun `should generate an encrypted password`() {
        val hash = PasswordUtils().generateBcrypt(password)
        Assertions.assertTrue(bCryptEncoder.matches(password, hash))
    }
}