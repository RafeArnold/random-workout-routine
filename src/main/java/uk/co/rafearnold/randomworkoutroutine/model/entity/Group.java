package uk.co.rafearnold.randomworkoutroutine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.List;

@Entity(name = "grouping")
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends Item {

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ExerciseOption> exerciseOptions;

    @Transient
    @JsonIgnore
    private double[] optionWeights;
}
