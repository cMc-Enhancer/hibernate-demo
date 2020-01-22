package com.enhancer.hibernate_demo.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class One {

    @Id
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "one_id", referencedColumnName = "id")
    private Set<Many> manySet = new HashSet<>();

    public One(Long id) {
        this.id = id;
    }
}
