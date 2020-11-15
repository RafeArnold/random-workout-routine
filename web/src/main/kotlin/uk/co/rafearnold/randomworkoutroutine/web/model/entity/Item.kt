package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import uk.co.rafearnold.randomworkoutroutine.model.Item
import java.util.*
import javax.persistence.*

/**
 * An implementation of [Item] for JPA.
 *
 * @property tags This is mutable to avoid JDBC errors when saving items.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Item(
    @Id override var id: UUID = UUID.randomUUID(),
    @Column(unique = true, nullable = false) override var name: String = "",
    tags: MutableSet<String> = mutableSetOf()
) : Item {

    @ElementCollection
    override var tags: MutableSet<String> = formatTags(tags)
        /**
         * Removes blank strings, trims and converts remaining strings to lowercase.
         */
        set(value) {
            field = formatTags(value)
        }

    private fun formatTags(tags: Set<String>): MutableSet<String> = tags
        .filter { tag: String -> !tag.isBlank() }
        .map { obj: String -> obj.trim() }
        .map { obj: String -> obj.toLowerCase() }
        .toMutableSet()
}
