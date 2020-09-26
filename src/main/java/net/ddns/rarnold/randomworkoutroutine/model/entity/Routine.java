package net.ddns.rarnold.randomworkoutroutine.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Routine extends Item {

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Group> groups;
}
