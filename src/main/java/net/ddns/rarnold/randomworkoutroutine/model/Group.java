package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "grouping")
@Data
@NoArgsConstructor
public class Group {
    @EmbeddedId
    private GroupId id;

    @ManyToOne
    @MapsId("routineName")
    private Routine routine;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ExerciseOption> exerciseOptions;

    public Group(Routine routine, int index, List<ExerciseOption> exerciseOptions) {
        this.id = new GroupId(routine.getName(), index);
        this.routine = routine;
        this.exerciseOptions = exerciseOptions;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupId implements Serializable {
        private String routineName;
        private int index;
    }
}
