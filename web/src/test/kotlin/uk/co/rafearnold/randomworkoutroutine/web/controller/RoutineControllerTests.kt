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
@ContextConfiguration(initializers = [RoutineControllerTests.Companion.Initializer::class])
internal open class RoutineControllerTests {

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
    private lateinit var repository: RoutineRepository

    @Autowired
    private lateinit var exerciseRepository: ExerciseRepository

    @Autowired
    private lateinit var exerciseOptionRepository: ExerciseOptionRepository

    @Autowired
    private lateinit var groupRepository: GroupRepository

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    open fun beforeEach() {
        clearAllMocks()
        repository.deleteAll()
        exerciseRepository.deleteAll()
        exerciseOptionRepository.deleteAll()
        groupRepository.deleteAll()
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id corresponds to a real item then that item is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercises: List<Exercise> = listOf(
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise3Tag1", "test_exercise3Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise4Tag1", "test_exercise4Tag2"))
        )
        val routine = Routine(
            id, randomString(), mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[0],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[1],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                ),
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[2],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[3],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                )
            )
        )

        // Save item.
        exerciseRepository.saveAll(exercises)
        routine.groups.forEach {
            exerciseOptionRepository.saveAll(it.exercises)
            groupRepository.save(it)
        }
        repository.save(routine)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(routine)))
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id does not correspond to a real item then a 404 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()

        // Make sure item does not exist.
        assertFalse(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/$id"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `items can be saved`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercises: List<Exercise> = listOf(
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise3Tag1", "test_exercise3Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise4Tag1", "test_exercise4Tag2"))
        )
        val routine = Routine(
            id, randomString(), mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[0],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[1],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                ),
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[2],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[3],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                )
            )
        )

        // Save groups and exercises.
        exerciseRepository.saveAll(exercises)

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/routine/save")
                    .content(objectMapper.writeValueAsBytes(routine))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been saved.
        val optional: Optional<Routine> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(routine, optional.get())
    }

    @Test
    @Transactional
    open fun `when saving an item and the id corresponds to an existing item then that item is overwritten`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val name: String = randomString()
        val existingRoutine = Routine(id, name)
        val newRoutine = Routine(id, name)

        // Save existing item.
        repository.save(existingRoutine)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/routine/save")
                .content(objectMapper.writeValueAsBytes(newRoutine))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been overwritten.
        assertEquals(1, repository.count())
        val optional: Optional<Routine> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(newRoutine, optional.get())
    }

    @Test
    open fun `when saving an item with the same name as another item but different id then a 409 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val name: String = randomString()
        val existingRoutine = Routine(id, name)
        val newRoutine = Routine(UUID.randomUUID(), name)

        // Save existing item.
        repository.save(existingRoutine)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/routine/save")
                .content(objectMapper.writeValueAsBytes(newRoutine))
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
        val exercises: List<Exercise> = listOf(
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise3Tag1", "test_exercise3Tag2")),
            Exercise(name = randomString(), tags = mutableSetOf("test_exercise4Tag1", "test_exercise4Tag2"))
        )
        val routine = Routine(
            id, " ", mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[0],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[1],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                ),
                Group(
                    exercises = mutableListOf(
                        ExerciseOption(
                            exercise = exercises[2],
                            repCountLowerBound = 15,
                            repCountUpperBound = 35
                        ),
                        ExerciseOption(
                            exercise = exercises[3],
                            repCountLowerBound = 5,
                            repCountUpperBound = 25
                        )
                    )
                )
            )
        )

        // Save groups and exercises.
        exerciseRepository.saveAll(exercises)

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/routine/save")
                .content(objectMapper.writeValueAsBytes(routine))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when saving an item that is not a routine then a 400 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exerciseOption = ExerciseOption(id)
        val group = Group(id)

        // Send requests and verify responses.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/routine/save")
                .content(objectMapper.writeValueAsBytes(exerciseOption))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/routine/save")
                .content(objectMapper.writeValueAsBytes(group))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when deleting an item and the provided id corresponds to a real item then that item is deleted`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val routine = Routine(id, randomString())

        // Save item.
        repository.save(routine)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/routine/delete/$id"))
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/routine/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Transactional
    open fun `when deleting an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/routine/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `all item names can be retrieved`() {
        // Initialise values.
        val routines: List<Routine> = listOf(
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            ),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            ),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            ),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            ),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            ),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_tag1", "test_tag2")
            )
        )

        // Save items.
        repository.saveAll(routines)

        // Make sure items have been saved.
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(routines.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/names"))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("[]", true))
    }

    @Test
    @Transactional
    open fun `when retrieving item names then only groups are returned`() {
        // Initialise values.
        val routines: List<Routine> = listOf(
            Routine(name = randomString()),
            Routine(name = randomString()),
            Routine(name = randomString())
        )
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

        // Save items.
        routines.forEach { repository.save(it) }
        exercises.forEach { exerciseRepository.save(it) }
        exerciseOptions.forEach { exerciseOptionRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(exerciseRepository.existsById(it.id)) }
        exerciseOptions.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(routines.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `items can be searched for by name or tag`() {
        // Initialise values.
        val name: String = randomString()
        val routines: List<Routine> = listOf(
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf(name, "test_routine1tag2")),
            Routine(
                UUID.randomUUID(), name,
                mutableSetOf("test_routine2tag1", "test_routine2tag2")),
            Routine(
                UUID.randomUUID(), randomString(),
                mutableSetOf("test_routine3tag1", "test_routine3tag2")
            ),
        )

        // Save items.
        routines.forEach { repository.save(it) }

        // Make sure items have been saved.
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(routines[0], routines[1]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/routine/search")
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
        val routines: List<Routine> = listOf(
            Routine(UUID.randomUUID(), randomString(), mutableSetOf(name1, "test_routine1tag2")),
            Routine(UUID.randomUUID(), name1, mutableSetOf("test_routine2tag1", "test_routine2tag2")),
            Routine(UUID.randomUUID(), name2, mutableSetOf("test_routine3tag1", "test_routine3tag2")),
        )

        // Save items.
        routines.forEach { repository.save(it) }

        // Make sure items have been saved.
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(routines[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/routine/search")
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
        val routines: List<Routine> = listOf(
            Routine(UUID.randomUUID(), randomString(), mutableSetOf("test_routine1tag1", "test_routine1tag2")),
            Routine(UUID.randomUUID(), randomString(), mutableSetOf("test_routine2tag1", "test_routine2tag2")),
            Routine(UUID.randomUUID(), randomString(), mutableSetOf("test_routine3tag1", "test_routine3tag2")),
        )

        // Save items.
        routines.forEach { repository.save(it) }

        // Make sure items have been saved.
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(routines.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/routine/search"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items then only groups are returned`() {
        // Initialise values.
        val name: String = randomString()
        val routines: List<Routine> = listOf(
            Routine(name = name),
            Routine(name = randomString()),
            Routine(name = randomString())
        )
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

        // Save items.
        routines.forEach { repository.save(it) }
        exercises.forEach { exerciseRepository.save(it) }
        exerciseOptions.forEach { exerciseOptionRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(exerciseRepository.existsById(it.id)) }
        exerciseOptions.forEach { assertTrue(exerciseOptionRepository.existsById(it.id)) }
        routines.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(routines[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/routine/search")
                .queryParam("term", name)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    open fun `when saving a routine with groups that have not been saved then these groups are also saved and their exercise options`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercises: List<Exercise> = listOf(
            Exercise(name = randomString()),
            Exercise(name = randomString()),
            Exercise(name = randomString()),
            Exercise(name = randomString())
        )
        val groups: MutableList<Group> = mutableListOf(
            Group(
                exercises = mutableListOf(
                    ExerciseOption(exercise = exercises[0], repCountLowerBound = 0, repCountUpperBound = 1),
                    ExerciseOption(exercise = exercises[1], repCountLowerBound = 1, repCountUpperBound = 2)
                )
            ),
            Group(
                exercises = mutableListOf(
                    ExerciseOption(exercise = exercises[2], repCountLowerBound = 2, repCountUpperBound = 3),
                    ExerciseOption(exercise = exercises[3], repCountLowerBound = 3, repCountUpperBound = 4)
                )
            )
        )
        val routine = Routine(id, randomString(), mutableSetOf("test_tag1", "test_tag2"), groups)

        // Save exercises.
        exerciseRepository.saveAll(exercises)

        // Make sure exercises have been saved.
        exercises.forEach { assertTrue(exerciseRepository.existsById(it.id)) }

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/routine/save")
                    .content(objectMapper.writeValueAsBytes(routine))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been saved.
        assertTrue(repository.existsById(id))
    }

    private fun randomString(): String = UUID.randomUUID().toString()
}
