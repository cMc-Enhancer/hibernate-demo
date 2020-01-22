package com.enhancer.hibernate_demo;

import com.enhancer.hibernate_demo.entity.Many;
import com.enhancer.hibernate_demo.entity.One;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManagerFactory;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
public class OneToManyUpdateBugTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void oneToManyTest() {
        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = new One(1L);
            Many many = new Many();
            many.setOneId(one.getId());
            many.setValue(1);
            one.getManySet().add(many);
            entityManager.persist(one);
            log.info("First unit of work done");
        });

        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = entityManager.find(One.class, 1L);
            Many existingMany = one.getManySet().iterator().next();
//            existingMany.setValue(2);   不修改已存在的many实体则不会执行多余的update引起异常
            Many newMany = new Many();
            newMany.setOneId(one.getId());
            one.getManySet().add(newMany);
            log.info("Second unit of work done");
        });

        // 修改已存在的many实体，并添加一个新的
        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = entityManager.find(One.class, 1L);
            Many existingMany = one.getManySet().iterator().next();
            existingMany.setValue(2);
            entityManager.flush();
            log.info("Third unit of work, updating done");

            Many newMany = new Many();
            newMany.setOneId(one.getId());
            one.getManySet().add(newMany);

            entityManager.flush(); // Throws ConstraintViolationException: NULL not allowed for column "ONE_ID"
        });
    }

    @Test
    public void solutionTest() {
        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = new One(1L);
            Many many = new Many();
            many.setOneId(one.getId());
            many.setValue(1);
            one.getManySet().add(many);
            entityManager.persist(one);
            log.info("First unit of work done");
        });

        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = entityManager.find(One.class, 1L);
            Many existingMany = one.getManySet().iterator().next();
            existingMany.setValue(2);
            log.info("Third unit of work, updating done");

            Many newMany = new Many();
            newMany.setOneId(one.getId());
            entityManager.persist(newMany);
        });

        doInJPA(() -> entityManagerFactory, entityManager -> {
            One one = entityManager.find(One.class, 1L);
            Set<Many> manySet = one.getManySet();
            assertThat(manySet, hasSize(2));
            for (Many many : manySet) {
                if (many.getId().equals(1L)) {
                    assertThat(many.getValue(), is(2));
                } else if (many.getId().equals(2L)) {
                } else {
                    fail();
                }
            }
        });
    }
}
