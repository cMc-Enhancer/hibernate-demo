package com.enhancer.hibernate_demo.entity.repository;

import com.enhancer.hibernate_demo.entity.Entity;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Getter
public class AutoIncrementIdEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public void save(Entity entity){
        entityManager.persist(entity);
    }

}
