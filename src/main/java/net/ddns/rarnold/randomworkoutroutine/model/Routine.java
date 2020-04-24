package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Routine {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Group> groups;

    public Routine(String name, List<Group> groups) {
        this.name = name;
        this.groups = groups;
    }
}
