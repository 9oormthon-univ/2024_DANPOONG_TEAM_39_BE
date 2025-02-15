package com.example._2024_danpoong_team_39_be.careAssignment;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.example._2024_danpoong_team_39_be.domain.CareRecipient;
import com.example._2024_danpoong_team_39_be.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareAssignmentRepository extends JpaRepository<CareAssignment, Long> {

    Optional<CareAssignment> findById(Long id);


    Optional<CareAssignment> findByEmail(String email);

    CareAssignment findCareAssignmentByEmail(String email);

}
