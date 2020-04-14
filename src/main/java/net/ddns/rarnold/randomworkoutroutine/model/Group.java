package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity(name = "grouping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ExerciseOption> exerciseOptions;
}
