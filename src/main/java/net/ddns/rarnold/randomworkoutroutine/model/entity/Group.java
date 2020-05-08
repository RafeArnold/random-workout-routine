package net.ddns.rarnold.randomworkoutroutine.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "grouping")
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ExerciseOption> exerciseOptions;

    public Group(String name, List<ExerciseOption> exerciseOptions) {
        this.name = name;
        this.exerciseOptions = exerciseOptions;
    }
}
