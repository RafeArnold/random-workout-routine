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
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Exercise
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.ExerciseOption
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Group
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseOptionRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.ExerciseRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.GroupRepository
import uk.co.rafearnold.randomworkoutroutine.web.repository.RoutineRepository
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = [ExerciseControllerTests.Companion.Initializer::class])
internal open class ExerciseControllerTests {

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
    private lateinit var repository: ExerciseRepository

    @Autowired
    private lateinit var exerciseOptionRepository: ExerciseOptionRepository

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    private lateinit var routineRepository: RoutineRepository

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    open fun beforeEach() {
        clearAllMocks()
        repository.deleteAll()
        exerciseOptionRepository.deleteAll()
        groupRepository.deleteAll()
        routineRepository.deleteAll()
    }

    @Test
    open fun `when retrieving an item and the provided id corresponds to a real item then that item is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = Exercise(id, randomString(), mutableSetOf("test_tag1", "test_tag2"))

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
        val exercise = Exercise(id, randomString(), mutableSetOf("test_tag1", "test_tag2"))

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/exercise/save")
                    .content(objectMapper.writeValueAsBytes(exercise))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been saved.
        val optional: Optional<Exercise> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(exercise, optional.get())
    }

    @Test
    @Transactional
    open fun `when saving an item and the id corresponds to an existing item then that item is overwritten`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val existingExercise = Exercise(id, randomString(), mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"))
        val newExercise = Exercise(id, randomString(), mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"))

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
        val optional: Optional<Exercise> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(newExercise, optional.get())
    }

    @Test
    open fun `when saving an item with the same name as another item but different id then a 409 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val name: String = randomString()
        val existingExercise = Exercise(id, name)
        val newExercise = Exercise(UUID.randomUUID(), name)

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
        val exercise = Exercise(id, " ", mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"))

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
        val exerciseOption = ExerciseOption(id)
        val group = Group(id)
        val routine = Routine(id, randomString())

        // Send requests and verify responses.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/exercise/save")
                .content(objectMapper.writeValueAsBytes(exerciseOption))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
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
        val exercise = Exercise(id, randomString(), mutableSetOf("test_tag1", "test_tag2"))

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
        val exercise = Exercise(id, randomString())
        val otherExercise1 = Exercise(name = randomString())
        val otherExercise2 = Exercise(name = randomString())
        val otherExerciseOption1 =
            ExerciseOption(exercise = otherExercise1, repCountLowerBound = 25, repCountUpperBound = 40)
        val otherExerciseOption2 =
            ExerciseOption(exercise = otherExercise2, repCountLowerBound = 0, repCountUpperBound = 1)
        val group1 = Group(
            exercises = mutableListOf(
                ExerciseOption(exercise = exercise, repCountLowerBound = 5, repCountUpperBound = 35),
                otherExerciseOption1
            )
        )
        val group2 = Group(
            exercises = mutableListOf(
                otherExerciseOption2,
                ExerciseOption(exercise = exercise, repCountLowerBound = 15, repCountUpperBound = 25)
            )
        )

        // Save the items.
        repository.save(exercise)
        repository.save(otherExercise1)
        repository.save(otherExercise2)
        exerciseOptionRepository.saveAll(group1.exercises)
        exerciseOptionRepository.saveAll(group2.exercises)
        groupRepository.save(group1)
        groupRepository.save(group2)

        // Make sure items have been saved.
        assertTrue(repository.existsById(id))
        assertTrue(repository.existsById(otherExercise1.id))
        assertTrue(repository.existsById(otherExercise2.id))
        assertTrue(groupRepository.existsById(group1.id))
        group1.exercises.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }
        assertTrue(groupRepository.existsById(group2.id))
        group2.exercises.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been deleted.
        assertFalse(repository.existsById(id))

        // Verify item has been removed from groups.
        val updatedGroup1: Optional<Group> = groupRepository.findById(group1.id)
        assertTrue(updatedGroup1.isPresent)
        assertEquals(listOf(otherExerciseOption1), updatedGroup1.get().exercises)
        val updatedGroup2: Optional<Group> = groupRepository.findById(group2.id)
        assertTrue(updatedGroup2.isPresent)
        assertEquals(listOf(otherExerciseOption2), updatedGroup2.get().exercises)
    }

    @Test
    @Transactional
    open fun `all item names can be retrieved`() {
        // Initialise values.
        val exercises: List<Exercise> = listOf(
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_tag1", "test_tag2"))
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
        val exercises: List<Exercise> = listOf(
            Exercise(name = randomString()),
            Exercise(name = randomString()),
            Exercise(name = randomString())
        )
        val exerciseOptions: List<ExerciseOption> = listOf(
            ExerciseOption(exercise = exercises[0]),
            ExerciseOption(exercise = exercises[1]),
            ExerciseOption(exercise = exercises[2])
        )
        val routines: List<Routine> = listOf(
            Routine(name = randomString()),
            Routine(name = randomString()),
            Routine(name = randomString())
        )

        // Save items.
        exercises.forEach { repository.save(it) }
        exerciseOptions.forEach { exerciseOptionRepository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }
        exerciseOptions.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }
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
        val name: String = randomString()
        val exercises: List<Exercise> = listOf(
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf(name, "test_exercise1tag2")),
            Exercise(UUID.randomUUID(), name, mutableSetOf("test_exercise2tag1", "test_exercise2tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_exercise3tag1", "test_exercise3tag2"))
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
                .queryParam("term", name)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and exclusion terms are provided then only items matching one of those exact names are excluded`() {
        // Initialise values.
        val name1: String = randomString()
        val name2: String = randomString()
        val exercises: List<Exercise> = listOf(
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf(name1, "test_exercise1tag2")),
            Exercise(UUID.randomUUID(), name1, mutableSetOf("test_exercise2tag1", "test_exercise2tag2")),
            Exercise(UUID.randomUUID(), name2, mutableSetOf("test_exercise3tag1", "test_exercise3tag2"))
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
                .queryParam("exclude", name1, name2)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and no search term or exclusion terms is provided then all items are returned`() {
        // Initialise values.
        val exercises: List<Exercise> = listOf(
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_exercise2", "test_exercise1tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_exercise2tag1", "test_exercise2tag2")),
            Exercise(UUID.randomUUID(), randomString(), mutableSetOf("test_exercise3tag1", "test_exercise3tag2"))
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
        val name = randomString()
        val exercises: List<Exercise> = listOf(
            Exercise(name = name),
            Exercise(name = randomString()),
            Exercise(name = randomString())
        )
        val exerciseOptions: List<ExerciseOption> = listOf(
            ExerciseOption(exercise = exercises[0]),
            ExerciseOption(exercise = exercises[1]),
            ExerciseOption(exercise = exercises[2])
        )
        val routines: List<Routine> = listOf(
            Routine(name = name),
            Routine(name = randomString(), tags = mutableSetOf(name)),
            Routine(name = randomString())
        )

        // Save items.
        exercises.forEach { repository.save(it) }
        exerciseOptions.forEach { exerciseOptionRepository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(repository.existsById(it.id)) }
        exerciseOptions.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }
        routines.forEach { assertTrue(routineRepository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(exercises[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/exercise/search")
                .queryParam("term", name)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    private fun randomString() = UUID.randomUUID().toString()
}
