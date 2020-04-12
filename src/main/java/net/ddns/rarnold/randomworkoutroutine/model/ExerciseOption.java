package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ExerciseOption {
    @Id
    private String name;
    private int repCountLowerBound;
    private int repCountUpperBound;
}
