package uk.co.rafearnold.randomworkoutroutine.web.repository

import org.springframework.stereotype.Repository
import uk.co.rafearnold.randomworkoutroutine.web.model.entity.Routine

@Repository
interface RoutineRepository : ItemRepository<Routine>
