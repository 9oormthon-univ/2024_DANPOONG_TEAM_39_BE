package com.example._2024_danpoong_team_39_be.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByDate(LocalDate date);

    List<Calendar> findByDateAndId(LocalDate date, Long id);
    //돌보미 일정 주간 조회

    List<Calendar> findByDateIn(List<LocalDate> weekDates);

    // isShared가 true인 일정만 날짜별로 조회
    List<Calendar> findByDateAndIsSharedTrue(LocalDate date);

    // isShared가 true이고 startDate가 특정 주의 날짜 리스트에 포함된 일정 조회
    List<Calendar> findByDateInAndIsSharedTrue(List<LocalDate> weekDates);

}