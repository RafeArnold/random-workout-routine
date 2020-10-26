package uk.co.rafearnold.randomworkoutroutine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.rafearnold.randomworkoutroutine.model.Exercise;
import uk.co.rafearnold.randomworkoutroutine.util.RandomUtils;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ExerciseOption extends Item {

    @Column(nullable = false)
    private int repCountLowerBound;

    @Column(nullable = false)
    private int repCountUpperBound;

    @JsonIgnore
    public Exercise getExercise() {
        return new Exercise(name, RandomUtils.nextInt(repCountLowerBound, repCountUpperBound));
    }
}
