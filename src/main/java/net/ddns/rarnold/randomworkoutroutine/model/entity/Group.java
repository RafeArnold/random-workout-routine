package net.ddns.rarnold.randomworkoutroutine.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "grouping")
@Data
public class Group {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ExerciseOption> exerciseOptions;
}
