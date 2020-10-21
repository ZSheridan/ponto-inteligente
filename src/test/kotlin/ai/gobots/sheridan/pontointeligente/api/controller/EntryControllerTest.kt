package ai.gobots.sheridan.pontointeligente.api.controller

import ai.gobots.sheridan.pontointeligente.api.dto.EntryDto
import ai.gobots.sheridan.pontointeligente.api.response.Response
import ai.gobots.sheridan.pontointeligente.domain.document.Employee
import ai.gobots.sheridan.pontointeligente.domain.enum.ProfileEnum.ROLE_ADMIN
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EmployeeServiceImpl
import ai.gobots.sheridan.pontointeligente.domain.service.impl.EntryServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.validation.BindingResult

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureDataMongo
internal class EntryControllerTest(
) {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var entryServiceImpl: EntryServiceImpl

    @MockBean
    private lateinit var employeeServiceImpl: EmployeeServiceImpl

    @MockBean
    private lateinit var result: BindingResult

    private val employeeId = "E1"
    private val employee = Employee("Test", "test@test.com", "123", "123",
            ROLE_ADMIN, "123", id = employeeId)
    private val date = "2020-10-17 08:00:00"
    private val type = "BEGIN_WORKDAY"
    private val entryDto = EntryDto(date, type, employeeId)
    private val responseEntityOk = ResponseEntity.ok().body(Response(data = entryDto))
    private val responseEntityBadRequest = ResponseEntity.badRequest().body(Response<EntryDto>(errors = arrayListOf("Entry not found")))
    private val baseUrl = "/api/entries"


    // Ainda não funciona
    @Test
    @WithMockUser(username = "mockuser@email.com", roles = ["USER"])
    @Throws(Exception::class)
    fun `method 'add' should save and return an entry`() {

        given(entryServiceImpl.validateAndAddEntry(entryDto, result, employeeServiceImpl))
                .willReturn(responseEntityOk)

        mockMvc
                .perform(post(baseUrl)
                .content(jsonContent(entryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.date").value(date))
                .andExpect(jsonPath("$.data.type").value(type))
                .andExpect(jsonPath("$.data.employeeId").value(employeeId))
                .andExpect(jsonPath("$.errors").isEmpty)

    }

    @Test
    @Throws(Exception::class)
    fun `method 'add' should return a bad request for invalid employeeId`() {

    }

    @Test
    fun findById() {
    }

    @Test
    fun listByEmployeeId() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    private fun jsonContent(content: Any): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(content)
    }
}