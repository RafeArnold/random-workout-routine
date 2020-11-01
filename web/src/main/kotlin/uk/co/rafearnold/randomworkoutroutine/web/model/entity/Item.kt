package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Item
import java.util.*
import javax.persistence.*

/**
 * An implementation of [Item] for JPA.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Item(
    @Id @GeneratedValue override var id: UUID = UUID.randomUUID(),
    @Column(unique = true, nullable = false) override var name: String = "",
    tags: Set<String> = setOf()
) : Item {

    @ElementCollection
    override var tags: Set<String> = tags
        /**
         * Removes blank strings, trims and converts remaining strings to lowercase.
         */
        set(value) {
            field = value
                .filter { tag: String -> !tag.isBlank() }
                .map { obj: String -> obj.trim() }
                .map { obj: String -> obj.toLowerCase() }
                .toSet()
        }
}
