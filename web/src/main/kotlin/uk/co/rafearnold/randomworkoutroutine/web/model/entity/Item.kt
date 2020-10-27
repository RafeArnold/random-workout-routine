package uk.co.rafearnold.randomworkoutroutine.web.model.entity

import java.util.*
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Item(
        @Id @GeneratedValue open var id: UUID = UUID.randomUUID(),
        @Column(unique = true, nullable = false) open var name: String = "",
        tags: MutableSet<String> = mutableSetOf()
) {

    @ElementCollection
    open var tags: MutableSet<String> = tags
        /**
         * Sets [.tags]. Removes blank strings, trims and converts remaining strings to lowercase
         * and converts to an unmodifiable set.
         */
        set(value) {
            field = value.stream()
                    .filter { tag: String -> !tag.isBlank() }
                    .map { obj: String -> obj.trim { it <= ' ' } }
                    .map { obj: String -> obj.toLowerCase() }
                    .collect(Collectors.toUnmodifiableSet())
        }
}
