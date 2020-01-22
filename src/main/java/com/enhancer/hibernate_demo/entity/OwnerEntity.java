package com.enhancer.hibernate_demo.entity;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Set;

@Entity
public class OwnerEntity {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable
    private Set<BeanValidationDoNotEffectDdlGenerateForEmbeddable> beanValidationDoNotEffectDdlGenerateForEmbeddable;
}
