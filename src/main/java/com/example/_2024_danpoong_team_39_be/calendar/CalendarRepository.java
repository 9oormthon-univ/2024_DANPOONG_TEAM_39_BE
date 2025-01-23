package com.example._2024_danpoong_team_39_be.calendar;

import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.example._2024_danpoong_team_39_be.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    // isShared가 true인 모든 일정 조회
    List<Calendar> findByIsSharedTrue();

    List<Calendar> findByCareAssignment(CareAssignment careAssignment);
//    @Query("SELECT ca FROM CareAssignment ca " +
//            "JOIN ca.calendars c " +
//            "WHERE c.date = :date " +
//            "AND (c.startTime BETWEEN :startTime AND :endTime " +
//            "OR c.endTime BETWEEN :startTime AND :endTime)")
//    List<CareAssignment> findCareAssignmentsByDateAndTimeRange(@Param("date") LocalDate date,
//                                                               @Param("startTime") LocalTime startTime,
//                                                               @Param("endTime") LocalTime endTime);



    List<Calendar> findByDateAndId(LocalDate date, Long id);

    List<Calendar> findAllByCareAssignmentAndRepeatCycle(CareAssignment careAssignment, Calendar.RepeatCycle repeatCycle);

    List<Calendar> findByIsSharedTrueOrderByStartTime();

    @Query("SELECT c FROM Calendar c WHERE c.date = :date")
    List<Calendar> findByDate(@Param("date") LocalDate date);


}

