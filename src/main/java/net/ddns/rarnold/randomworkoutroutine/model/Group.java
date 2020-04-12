package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "grouping")
@Data
public class Group {
    @EmbeddedId
    private GroupId id;

    @ManyToOne
    @MapsId("routineName")
    private Routine routine;

    @ManyToMany
    private List<ExerciseOption> exerciseOptions;

    @Embeddable
    @Data
    public static class GroupId implements Serializable {
        private String routineName;
        private int index;
    }
}
