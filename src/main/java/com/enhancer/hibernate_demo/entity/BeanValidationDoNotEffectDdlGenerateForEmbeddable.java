package com.enhancer.hibernate_demo.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class BeanValidationDoNotEffectDdlGenerateForEmbeddable {

    @NotNull
    private String name;

    @Column(updatable = false)
    private String identifier;

}
