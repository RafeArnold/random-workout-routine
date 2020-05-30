package net.ddns.rarnold.randomworkoutroutine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.rarnold.randomworkoutroutine.model.Exercise;
import net.ddns.rarnold.randomworkoutroutine.util.RandomUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ExerciseOption {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int repCountLowerBound;

    @Column(nullable = false)
    private int repCountUpperBound;

    @JsonIgnore
    public Exercise getExercise() {
        return new Exercise(name, RandomUtils.nextInt(repCountLowerBound, repCountUpperBound));
    }
}
