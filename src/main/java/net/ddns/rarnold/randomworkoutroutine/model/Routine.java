package net.ddns.rarnold.randomworkoutroutine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routine {
    @Id
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Group> groups;
}
