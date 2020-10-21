package ai.gobots.sheridan.pontointeligente.domain.service.impl

import ai.gobots.sheridan.pontointeligente.domain.document.Entry
import ai.gobots.sheridan.pontointeligente.domain.enum.TypeEnum.BEGIN_WORKDAY
import ai.gobots.sheridan.pontointeligente.domain.repository.EntryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.validation.BindingResult
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
internal class EntryServiceImplTest {

    @Autowired
    private lateinit var entryServiceImpl: EntryServiceImpl

    @MockBean
    private lateinit var employeeServiceImpl: EmployeeServiceImpl

    @MockBean
    private lateinit var entryRepository: EntryRepository

    @MockBean
    private val result: BindingResult? = null

    private val id = "N1"
    private val date = Date()
    private val employeeId = "123456"
    private val entry = Entry(date, BEGIN_WORKDAY, employeeId)
    private val entryDto = entry.toEntryDto()
    private val idNotExistent = "none"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
                .given(entryRepository.save(any(Entry::class.java)))
                .willReturn(entry)
        BDDMockito
                .given<Page<Entry>>(entryRepository.findByEmployeeId(employeeId, PageRequest.of(0, 10)))
                .willReturn(PageImpl(ArrayList<Entry>()))
        BDDMockito
                .given(entryRepository.findById(id))
                .willReturn(Optional.of(entry))
    }

    @Test
    fun `should save and return an entry`() {
        val newEntry = entryServiceImpl.persist(entry)
        assertEquals(entry, newEntry)
        verify(entryRepository, times(1)).save(entry)
    }

    @Test
    fun `should return a list of entries by employeeId`() {
        val listEntries = entryServiceImpl.findByEmployeeId(employeeId, PageRequest.of(0, 10))
        assertNotNull(listEntries)
        verify(entryRepository, times(1)).findByEmployeeId(employeeId, PageRequest.of(0, 10))
    }

    @Test
    fun `should return an entry by its id`() {
        val newEntry = entryServiceImpl.findById(id)
        assertEquals(entry, newEntry)
        verify(entryRepository, times(1)).findById(id)
    }

    @Test
    fun `should remove an entry by its id`() {
        entryServiceImpl.remove(id)
        verify(entryRepository, times(1)).deleteById(id)
    }

    @Test
    fun `this deleteById call should return a no content status`() {
        val responseEntity = entryServiceImpl.deleteById(id)
        assertEquals(204, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).deleteById(id)
    }

    @Test
    fun `this deleteById call should return a bad request status`() {
        val responseEntity = entryServiceImpl.deleteById(idNotExistent)
        assertEquals(400, responseEntity.statusCode.value())
        verify(entryRepository, times(0)).deleteById(id)
    }

    @Test
    fun `getEntriesByEmployeeId should return an Ok status`() {
        val responseEntity =
                entryServiceImpl.getEntriesByEmployeeId(employeeId, 0, 1, "ASC", "id")
        assertEquals(200, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).findByEmployeeId(employeeId, PageRequest.of(0, 1, Sort.Direction.ASC, "id"))
    }

    @Test
    fun `this updateEntry call should return a bad request status`() {
        BDDMockito
                .given(result?.hasErrors())
                .willReturn(true)

        val responseEntity = entryServiceImpl.updateEntry(idNotExistent, entryDto, result!!)
        assertEquals(400, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).findById(idNotExistent)
        verify(entryRepository, times(0)).save(entryDto.toEntry())
    }

    @Test
    fun `this updateEntry call should return an Ok status`() {
        BDDMockito
                .given(result?.hasErrors())
                .willReturn(false)

        val responseEntity = entryServiceImpl.updateEntry(employeeId, entryDto, result!!)
        assertEquals(200, responseEntity.statusCode.value())
    }

    @Test
    fun `this validateAndAddEntry call should return a bad request status`() {
        BDDMockito
                .given(result?.hasErrors())
                .willReturn(true)

        val responseEntity = entryServiceImpl.validateAndAddEntry(entryDto, result!!, employeeServiceImpl)
        assertEquals(400, responseEntity.statusCode.value())
        verify(entryRepository, times(0)).save(entryDto.toEntry())
    }

    @Test
    fun `this validateAndAddEntry call should return an Ok status`() {
        BDDMockito
                .given(result?.hasErrors())
                .willReturn(false)

        val responseEntity = entryServiceImpl.validateAndAddEntry(entryDto, result!!, employeeServiceImpl)
        assertEquals(200, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).save(entryDto.toEntry())
    }

    @Test
    fun `this validateAndGetEntryById call should return a bad request status`() {
        val responseEntity = entryServiceImpl.validateAndGetEntryById(idNotExistent)
        assertEquals(400, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).findById(idNotExistent)
    }

    @Test
    fun `this validateAndGetEntryById call should return an Ok status`() {
        val responseEntity = entryServiceImpl.validateAndGetEntryById(id)
        assertEquals(200, responseEntity.statusCode.value())
        verify(entryRepository, times(1)).findById(id)
    }

}