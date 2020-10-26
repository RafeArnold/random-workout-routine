package uk.co.rafearnold.randomworkoutroutine.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Item {

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(unique = true, nullable = false)
    protected String name;

    @ElementCollection
    protected Set<String> tags;

    /**
     * Sets {@link #tags}. Removes blank strings, trims and converts remaining strings to lowercase
     * and converts to an unmodifiable set.
     */
    public void setTags(Collection<String> tags) {
        if (tags == null) {
            this.tags = Set.of();
        } else {
            this.tags = tags.stream()
                    .filter(tag -> !tag.isBlank())
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }
}
