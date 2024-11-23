package com.example._2024_danpoong_team_39_be.careAssignment;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareAssignmentRepository extends JpaRepository<CareAssignment, Long> {

    Optional<CareAssignment> findById(Long id);

}
