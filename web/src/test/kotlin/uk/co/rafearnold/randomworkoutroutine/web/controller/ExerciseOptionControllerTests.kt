package uk.co.rafearnold.randomworkoutroutine.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.clearAllMocks
import org.junit.ClassRule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainerProvider
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import uk.co.rafearnold.randomworkoutroutine.model.SimpleItemImpl
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.RoutineRepository
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = [ExerciseOptionControllerTests.Companion.Initializer::class])
internal open class ExerciseOptionControllerTests {

    companion object {

        @Container
        @ClassRule
        private val psqlContainer: JdbcDatabaseContainer<*> = PostgreSQLContainerProvider().newInstance()

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=" + psqlContainer.jdbcUrl,
                    "spring.datasource.username=" + psqlContainer.username,
                    "spring.datasource.password=" + psqlContainer.password
                ).applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repository: ExerciseOptionRepository

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    private lateinit var routineRepository: RoutineRepository

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    open fun beforeEach() {
        clearAllMocks()
        repository.deleteAll()
        groupRepository.deleteAll()
        routineRepository.deleteAll()
    }

    @Test
    open fun `when retrieving an item and the provided id corresponds to a real item then that item is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(id, "test_exercise", mutableSetOf("test_tag1", "test_tag2"), 5, 35)

        // Save item.
        repository.save(exercise)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(exercise), true))
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id does not correspond to a real item then a 404 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()

        // Make sure item does not exist.
        assertFalse(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/$id"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `items can be saved`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(id, "test_exercise", mutableSetOf("test_tag1", "test_tag2"), 5, 35)

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/exercise/save")
                    .content(objectMapper.writeValueAsBytes(exercise))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been saved.
        val optional: Optional<ExerciseOption> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(exercise, optional.get())
    }

    @Test
    @Transactional
    open fun `when saving an item and the id corresponds to an existing item then that item is overwritten`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val existingExercise = ExerciseOption(
            id, "test_exercise1",
            mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
            5, 35
        )
        val newExercise = ExerciseOption(
            id, "test_exercise2",
            mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"),
            15, 25
        )

        // Save existing item.
        repository.save(existingExercise)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(newExercise))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been overwritten.
        assertEquals(1, repository.count())
        val optional: Optional<ExerciseOption> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(newExercise, optional.get())
    }

    @Test
    open fun `when saving an item with the same name as another item but different id then a 409 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val existingExercise = ExerciseOption(id, "test_exercise")
        val newExercise = ExerciseOption(UUID.randomUUID(), "test_exercise")

        // Save existing item.
        repository.save(existingExercise)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(newExercise))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)

        // Assert original item is the only saved item.
        assertEquals(1, repository.count())
        assertTrue(repository.existsById(id))
    }

    @Test
    @Transactional
    open fun `when saving an item with a blank name then a 400 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(
            id, " ",
            mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
            5, 35
        )

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(exercise))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when saving an item that is not an exercise then a 400 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(id, "test_group")
        val routine = Routine(id, "test_routine")

        // Send requests and verify responses.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(group))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(routine))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    open fun `when deleting an item and the provided id corresponds to a real item then that item is deleted`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(id, "test_exercise", mutableSetOf("test_tag1", "test_tag2"), 5, 35)

        // Save item.
        repository.save(exercise)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been deleted.
        assertFalse(repository.existsById(id))
    }

    @Test
    @Transactional
    open fun `when deleting an item and the provided id does not correspond to a real item then there is no effect and 200 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()

        // Make sure item does not exist.
        assertFalse(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Transactional
    open fun `when deleting an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when an item is deleted then it is also removed from all groups that contain it`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(id, "test_exercise")
        val otherExercise1 = ExerciseOption(name = "test_otherExercise1")
        val otherExercise2 = ExerciseOption(name = "test_otherExercise2")
        val group1 = Group(name = "test_group1", exercises = mutableListOf(exercise, otherExercise1))
        val group2 = Group(name = "test_group2", exercises = mutableListOf(otherExercise2, exercise))

        // Save the items.
        repository.save(exercise)
        repository.save(otherExercise1)
        repository.save(otherExercise2)
        groupRepository.save(group1)
        groupRepository.save(group2)

        // Make sure items have been saved.
        assertTrue(repository.existsById(id))
        assertTrue(repository.existsById(otherExercise1.id))
        assertTrue(repository.existsById(otherExercise2.id))
        assertTrue(groupRepository.existsById(group1.id))
        assertTrue(groupRepository.existsById(group2.id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been deleted.
        assertFalse(repository.existsById(id))

        // Verify item has been removed from groups.
        val updatedGroup1: Optional<Group> = groupRepository.findById(group1.id)
        assertTrue(updatedGroup1.isPresent)
        assertEquals(listOf(otherExercise1), updatedGroup1.get().exercises)
        val updatedGroup2: Optional<Group> = groupRepository.findById(group2.id)
        assertTrue(updatedGroup2.isPresent)
        assertEquals(listOf(otherExercise2), updatedGroup2.get().exercises)
    }

    @Test
    @Transactional
    open fun `all item names can be retrieved`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(UUID.randomUUID(), "test_exercise1", mutableSetOf("test_tag1", "test_tag2"), 5, 35),
            ExerciseOption(UUID.randomUUID(), "test_exercise2", mutableSetOf("test_tag1", "test_tag2"), 5, 35),
            ExerciseOption(UUID.randomUUID(), "test_exercise3", mutableSetOf("test_tag1", "test_tag2"), 5, 35),
            ExerciseOption(UUID.randomUUID(), "test_exercise4", mutableSetOf("test_tag1", "test_tag2"), 5, 35),
            ExerciseOption(UUID.randomUUID(), "test_exercise5", mutableSetOf("test_tag1", "test_tag2"), 5, 35),
            ExerciseOption(UUID.randomUUID(), "test_exercise6", mutableSetOf("test_tag1", "test_tag2"), 5, 35)
        )

        // Save items.
        exercises.forEach { repository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(exercises.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when retrieving item names and there are no items in the database then an empty list is returned`() {
        // Make sure there are no items saved.
        assertEquals(0, repository.count())

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("[]", true))
    }

    @Test
    @Transactional
    open fun `when retrieving item names then only exercises are returned`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(name = "test_exercise1"),
            ExerciseOption(name = "test_exercise2"),
            ExerciseOption(name = "test_exercise3")
        )
        val groups: List<Group> = listOf(
            Group(name = "test_group1"),
            Group(name = "test_group2"),
            Group(name = "test_group3")
        )
        val routines: List<Routine> = listOf(
            Routine(name = "test_routine1"),
            Routine(name = "test_routine2"),
            Routine(name = "test_routine3")
        )

        // Save items.
        exercises.forEach { repository.save(it) }
        groups.forEach { groupRepository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }
        groups.forEach { assertTrue(groupRepository.existsById(it.id)) }
        routines.forEach { assertTrue(routineRepository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(exercises.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `items can be searched for by name or tag`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise1",
                mutableSetOf("test_exercise2", "test_exercise1tag2"),
                5,
                35
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise2",
                mutableSetOf("test_exercise2tag1", "test_exercise2tag2"),
                1,
                13
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise3",
                mutableSetOf("test_exercise3tag1", "test_exercise3tag2"),
                1,
                13
            )
        )

        // Save items.
        exercises.forEach { repository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(exercises[0], exercises[1]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exercise/search")
                .queryParam("term", "test_exercise2")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and exclusion terms are provided then only items matching one of those exact names are excluded`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise1",
                mutableSetOf("test_exercise2", "test_exercise1tag2"),
                5,
                35
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise2",
                mutableSetOf("test_exercise2tag1", "test_exercise2tag2"),
                1,
                13
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise3",
                mutableSetOf("test_exercise3tag1", "test_exercise3tag2"),
                1,
                13
            )
        )

        // Save items.
        exercises.forEach { repository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(exercises[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exercise/search")
                .queryParam("exclude", "test_exercise2", "test_exercise3")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and no search term or exclusion terms is provided then all items are returned`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise1",
                mutableSetOf("test_exercise2", "test_exercise1tag2"),
                5,
                35
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise2",
                mutableSetOf("test_exercise2tag1", "test_exercise2tag2"),
                1,
                13
            ),
            ExerciseOption(
                UUID.randomUUID(),
                "test_exercise3",
                mutableSetOf("test_exercise3tag1", "test_exercise3tag2"),
                1,
                13
            )
        )

        // Save items.
        exercises.forEach { repository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(exercises.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/search"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items then only exercises are returned`() {
        // Initialise values.
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(name = "test_exercise1"),
            ExerciseOption(name = "test_exercise2"),
            ExerciseOption(name = "test_exercise3")
        )
        val groups: List<Group> = listOf(
            Group(name = "test_exercise1"),
            Group(name = "test_group2"),
            Group(name = "test_group3")
        )
        val routines: List<Routine> = listOf(
            Routine(name = "test_exercise1"),
            Routine(name = "test_routine2"),
            Routine(name = "test_routine3")
        )

        // Save items.
        exercises.forEach { repository.save(it) }
        groups.forEach { groupRepository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }
        groups.forEach { assertTrue(groupRepository.existsById(it.id)) }
        routines.forEach { assertTrue(routineRepository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(exercises[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exercise/search")
                .queryParam("term", "test_exercise1")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }
}
