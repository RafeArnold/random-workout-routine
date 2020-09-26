package net.ddns.rarnold.randomworkoutroutine.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class Item {

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(unique = true, nullable = false)
    protected String name;
}
