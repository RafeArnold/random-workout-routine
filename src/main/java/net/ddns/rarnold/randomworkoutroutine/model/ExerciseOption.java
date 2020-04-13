package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseOption {
    @Id
    private String name;
    private int repCountLowerBound;
    private int repCountUpperBound;
}
