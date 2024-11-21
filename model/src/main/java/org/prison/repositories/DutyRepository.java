package org.prison.repositories;

import org.prison.model.staffs.Duty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DutyRepository extends JpaRepository<Duty, Integer> {

    List<Duty> findAllByDate(LocalDate date);

}
