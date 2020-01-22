package com.enhancer.hibernate_demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class TradeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false, updatable = false)
    private User user;

    @NotNull
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @Setter
    private int value;

    // ...other properties

    public TradeReport(@NotNull User user, @NotNull LocalDate date) {
        this.user = user;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeReport that = (TradeReport) o;
        return user.equals(that.user) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date);
    }
}
