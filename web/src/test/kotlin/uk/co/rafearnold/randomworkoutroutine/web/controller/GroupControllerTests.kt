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
@ContextConfiguration(initializers = [GroupControllerTests.Companion.Initializer::class])
internal open class GroupControllerTests {

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
    private lateinit var repository: GroupRepository

    @Autowired
    private lateinit var exerciseRepository: ExerciseOptionRepository

    @Autowired
    private lateinit var routineRepository: RoutineRepository

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    open fun beforeEach() {
        clearAllMocks()
        repository.deleteAll()
        exerciseRepository.deleteAll()
        routineRepository.deleteAll()
    }

    @Test
    open fun `when retrieving an item and the provided id corresponds to a real item then that item is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(
            id,
            "test_group",
            mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                ExerciseOption(
                    name = "test_exercise1",
                    tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
                    repCountLowerBound = 15,
                    repCountUpperBound = 35
                ),
                ExerciseOption(
                    name = "test_exercise2",
                    tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"),
                    repCountLowerBound = 5,
                    repCountUpperBound = 25
                )
            )
        )

        // Save item.
        group.exercises.forEach { exercise -> exerciseRepository.save(exercise) }
        repository.save(group)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(group)))
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id does not correspond to a real item then a 404 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()

        // Make sure item does not exist.
        assertFalse(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/$id"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    open fun `when retrieving an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `items can be saved`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(
            id,
            "test_group",
            mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                ExerciseOption(
                    name = "test_exercise1",
                    tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
                    repCountLowerBound = 15,
                    repCountUpperBound = 35
                ),
                ExerciseOption(
                    name = "test_exercise2",
                    tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"),
                    repCountLowerBound = 5,
                    repCountUpperBound = 25
                )
            )
        )

        // Save exercises.
        group.exercises.forEach { exercise -> exerciseRepository.save(exercise) }

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/group/save")
                    .content(objectMapper.writeValueAsBytes(group))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been saved.
        val optional: Optional<Group> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(group, optional.get())
    }

    @Test
    @Transactional
    open fun `when saving an item and the id corresponds to an existing item then that item is overwritten`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val existingGroup = Group(id, "test_group1")
        val newGroup = Group(id, "test_group2")

        // Save existing item.
        repository.save(existingGroup)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/group/save")
                .content(objectMapper.writeValueAsBytes(newGroup))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been overwritten.
        assertEquals(1, repository.count())
        val optional: Optional<Group> = repository.findById(id)
        assertTrue(optional.isPresent)
        assertEquals(newGroup, optional.get())
    }

    @Test
    open fun `when saving an item with the same name as another item but different id then a 409 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val existingGroup = Group(id, "test_group")
        val newGroup = Group(UUID.randomUUID(), "test_group")

        // Save existing item.
        repository.save(existingGroup)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/group/save")
                .content(objectMapper.writeValueAsBytes(newGroup))
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
        val group = Group(
            id,
            " ",
            mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                ExerciseOption(
                    name = "test_exercise1",
                    tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
                    repCountLowerBound = 15,
                    repCountUpperBound = 35
                ),
                ExerciseOption(
                    name = "test_exercise2",
                    tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"),
                    repCountLowerBound = 5,
                    repCountUpperBound = 25
                )
            )
        )

        // Save exercises.
        group.exercises.forEach { exercise -> exerciseRepository.save(exercise) }

        // Send request and verify response.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/group/save")
                .content(objectMapper.writeValueAsBytes(group))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when saving an item that is not a group then a 400 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val exercise = ExerciseOption(id, "test_exercise")
        val routine = Routine(id, "test_routine")

        // Send requests and verify responses.
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/group/save")
                .content(objectMapper.writeValueAsBytes(exercise))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/group/save")
                .content(objectMapper.writeValueAsBytes(routine))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    open fun `when deleting an item and the provided id corresponds to a real item then that item is deleted`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(id, "test_group")

        // Save item.
        repository.save(group)

        // Make sure item has been saved.
        assertTrue(repository.existsById(id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/$id"))
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Transactional
    open fun `when deleting an item and the provided id is not a uuid then a 400 is returned`() {
        // Initialise values.
        val id = "not_a_uuid"

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    @Transactional
    open fun `when an item is deleted then it is also removed from all routines that contain it`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(id, "test_exercise")
        val otherGroup1 = Group(name = "test_otherGroup1")
        val otherGroup2 = Group(name = "test_otherGroup2")
        val routine1 = Routine(name = "test_routine1", groups = mutableListOf(group, otherGroup1))
        val routine2 = Routine(name = "test_routine2", groups = mutableListOf(otherGroup2, group))

        // Save the items.
        repository.save(group)
        repository.save(otherGroup1)
        repository.save(otherGroup2)
        routineRepository.save(routine1)
        routineRepository.save(routine2)

        // Make sure items have been saved.
        assertTrue(repository.existsById(id))
        assertTrue(repository.existsById(otherGroup1.id))
        assertTrue(repository.existsById(otherGroup2.id))
        assertTrue(routineRepository.existsById(routine1.id))
        assertTrue(routineRepository.existsById(routine2.id))

        // Send request and verify response.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/$id"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // Verify item has been deleted.
        assertFalse(repository.existsById(id))

        // Verify item has been removed from groups.
        val updatedRoutine1: Optional<Routine> = routineRepository.findById(routine1.id)
        assertTrue(updatedRoutine1.isPresent)
        assertEquals(listOf(otherGroup1), updatedRoutine1.get().groups)
        val updatedRoutine2: Optional<Routine> = routineRepository.findById(routine2.id)
        assertTrue(updatedRoutine2.isPresent)
        assertEquals(listOf(otherGroup2), updatedRoutine2.get().groups)
    }

    @Test
    @Transactional
    open fun `all item names can be retrieved`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(UUID.randomUUID(), "test_group1", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group1exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group1exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25))),
            Group(UUID.randomUUID(), "test_group2", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group2exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group2exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25))),
            Group(UUID.randomUUID(), "test_group3", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group3exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group3exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25))),
            Group(UUID.randomUUID(), "test_group4", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group4exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group4exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25))),
            Group(UUID.randomUUID(), "test_group5", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group5exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group5exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25))),
            Group(UUID.randomUUID(), "test_group6", mutableSetOf("test_tag1", "test_tag2"), mutableListOf(ExerciseOption(name = "test_group6exercise1", tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"), repCountLowerBound = 15, repCountUpperBound = 35), ExerciseOption(name = "test_group6exercise2", tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"), repCountLowerBound = 5, repCountUpperBound = 25)))
        )

        // Save items.
        groups.forEach {
            it.exercises.forEach { exercise -> exerciseRepository.save(exercise) }
            repository.save(it)
        }

        // Make sure items have been saved.
        groups.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(groups.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/names"))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("[]", true))
    }

    @Test
    open fun `when retrieving item names then only groups are returned`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(name = "test_group1"),
            Group(name = "test_group2"),
            Group(name = "test_group3")
        )
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(name = "test_exercise1"),
            ExerciseOption(name = "test_exercise2"),
            ExerciseOption(name = "test_exercise3")
        )
        val routines: List<Routine> = listOf(
            Routine(name = "test_routine1"),
            Routine(name = "test_routine2"),
            Routine(name = "test_routine3")
        )

        // Save items.
        exercises.forEach { exerciseRepository.save(it) }
        groups.forEach { repository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        exercises.forEach { assertTrue(exerciseRepository.existsById(it.id)) }
        groups.forEach { assertTrue(repository.existsById(it.id)) }
        routines.forEach { assertTrue(routineRepository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String = objectMapper.writeValueAsString(groups.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/names"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `items can be searched for by name or tag`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(UUID.randomUUID(), "test_group1", mutableSetOf("test_group2", "test_group1tag2")),
            Group(UUID.randomUUID(), "test_group2", mutableSetOf("test_group2tag1", "test_group2tag2")),
            Group(UUID.randomUUID(), "test_group3", mutableSetOf("test_group3tag1", "test_group3tag2"))
        )

        // Save items.
        groups.forEach { repository.save(it) }

        // Make sure items have been saved.
        groups.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(groups[0], groups[1]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/group/search")
                .queryParam("term", "test_group2")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and exclusion terms are provided then only items matching one of those exact names are excluded`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(UUID.randomUUID(), "test_group1", mutableSetOf("test_group2", "test_group1tag2")),
            Group(UUID.randomUUID(), "test_group2", mutableSetOf("test_group2tag1", "test_group2tag2")),
            Group(UUID.randomUUID(), "test_group3", mutableSetOf("test_group3tag1", "test_group3tag2"))
        )

        // Save items.
        groups.forEach { repository.save(it) }

        // Make sure items have been saved.
        groups.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(groups[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/group/search")
                .queryParam("exclude", "test_group2", "test_group3")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    @Transactional
    open fun `when searching for items and no search term or exclusion terms is provided then all items are returned`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(UUID.randomUUID(), "test_group1", mutableSetOf("test_group2", "test_group1tag2")),
            Group(UUID.randomUUID(), "test_group2", mutableSetOf("test_group2tag1", "test_group2tag2")),
            Group(UUID.randomUUID(), "test_group3", mutableSetOf("test_group3tag1", "test_group3tag2"))
        )

        // Save items.
        groups.forEach { repository.save(it) }

        // Make sure items have been saved.
        groups.forEach { assertTrue(repository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(groups.map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(MockMvcRequestBuilders.get("/api/group/search"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    open fun `when searching for items then only groups are returned`() {
        // Initialise values.
        val groups: List<Group> = listOf(
            Group(name = "test_group1"),
            Group(name = "test_group2"),
            Group(name = "test_group3")
        )
        val exercises: List<ExerciseOption> = listOf(
            ExerciseOption(name = "test_group1"),
            ExerciseOption(name = "test_exercise2"),
            ExerciseOption(name = "test_exercise3")
        )
        val routines: List<Routine> = listOf(
            Routine(name = "test_group1"),
            Routine(name = "test_routine2"),
            Routine(name = "test_routine3")
        )

        // Save items.
        groups.forEach { repository.save(it) }
        exercises.forEach { exerciseRepository.save(it) }
        routines.forEach { routineRepository.save(it) }

        // Make sure items have been saved.
        groups.forEach { assertTrue(repository.existsById(it.id)) }
        exercises.forEach { assertTrue(exerciseRepository.existsById(it.id)) }
        routines.forEach { assertTrue(routineRepository.existsById(it.id)) }

        // Send request and verify response.
        val expectedJson: String =
            objectMapper.writeValueAsString(listOf(groups[0]).map { SimpleItemImpl(it.id, it.name) })
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/group/search")
                .queryParam("term", "test_group1")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedJson, true))
    }

    @Test
    fun `when saving a group with exercises that have not been saved then nothing is saved and a 404 is returned`() {
        // Initialise values.
        val id: UUID = UUID.randomUUID()
        val group = Group(
            id,
            "test_group",
            mutableSetOf("test_tag1", "test_tag2"),
            mutableListOf(
                ExerciseOption(
                    name = "test_exercise1",
                    tags = mutableSetOf("test_exercise1Tag1", "test_exercise1Tag2"),
                    repCountLowerBound = 15,
                    repCountUpperBound = 35
                ),
                ExerciseOption(
                    name = "test_exercise2",
                    tags = mutableSetOf("test_exercise2Tag1", "test_exercise2Tag2"),
                    repCountLowerBound = 5,
                    repCountUpperBound = 25
                )
            )
        )

        // Send request and verify response.
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/group/save")
                    .content(objectMapper.writeValueAsBytes(group))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        // Verify item has not been saved.
        assertEquals(0, repository.count())
    }
}
