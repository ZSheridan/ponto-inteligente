package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.domain.document.Company
import ai.gobots.sheridan.pontointeligente.domain.repository.CompanyRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
internal class CompanyServiceImplTest {

    @Autowired
    private lateinit var companyServiceImpl: CompanyServiceImpl

    @MockBean
    private lateinit var companyRepository: CompanyRepository

    private val cnpj = "12.235.265/0001-33"
    private val company = Company("GoBots", cnpj)

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
                .given(companyRepository.findByCnpj(anyString()))
                .willReturn(company)
        BDDMockito
                .given(companyRepository.save(any(Company::class.java)))
                .willReturn(company)
    }

    @Test
    fun `should return a company by its CNPJ`() {
        val newCompany: Company? = companyServiceImpl.findByCnpj(cnpj)
        assertNotNull(newCompany)
        verify(companyRepository, times(1)).findByCnpj(cnpj)
    }

    @Test
    fun `should save and return a company`() {
        val newCompany = companyServiceImpl.persist(company)
        assertEquals(newCompany, company)
        verify(companyRepository, times(1)).save(company)
    }

}