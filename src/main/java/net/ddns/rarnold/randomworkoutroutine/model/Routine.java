package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Routine {
    @Id
    private String name;

    @OneToMany(mappedBy = "routine", fetch = FetchType.EAGER)
    private List<Group> groups;
}
