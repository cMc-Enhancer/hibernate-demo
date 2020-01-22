package com.enhancer.hibernate_demo;

import com.enhancer.hibernate_demo.entity.Many;
import com.enhancer.hibernate_demo.entity.One;
import com.enhancer.hibernate_demo.entity.TradeReport;
import com.enhancer.hibernate_demo.entity.User;
import com.enhancer.hibernate_demo.entity.repository.AutoIncrementIdEntityRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

@SpringBootTest
class HibernateDemoApplicationTests {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    AutoIncrementIdEntityRepository repository;

    @Before
    public void setup() {
        doInJPA(() -> entityManagerFactory, entityManager -> {
            User user = new User(1L);
            entityManager.persist(user);

            TradeReport report = new TradeReport(user, LocalDate.of(2019, 12, 26));
            entityManager.persist(report);
        });
    }

    @Test
    @Transactional(readOnly = true)
    public void equalsAndHashCodeImplementationProblemTest() {
        doInJPA(() -> entityManagerFactory, entityManager -> {
            List<TradeReport> reports = entityManager.createQuery("select r from TradeReport r", TradeReport.class)
//                    .setHint(QueryHints.HINT_READONLY, true)
                    .getResultList();

            TradeReport report = reports.get(0);
            report.setValue(10);
        });
    }

}
