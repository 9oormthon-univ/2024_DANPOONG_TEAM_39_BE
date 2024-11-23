package com.example._2024_danpoong_team_39_be.calendar;

import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    @Autowired
    private CalendarService careAssignmentService;

    @Autowired
    private CalendarRepository calendarRepository;
    // 특정 날짜의 세부 일정 조회(1개) 작동 o
    @GetMapping("/{date}/events/{eventId}")
    public List<Calendar> getDailyDetailEvents(@PathVariable LocalDate date, @PathVariable Long eventId) {
        return careAssignmentService.getDailyDetailEvents(date, eventId);
    }
    // 특정 날짜의 일정 리스트 조회(ex 2024-11-23에 있는 모든 a의 일정)
    @GetMapping("/{careAssignmentId}/{date}")
    public List<Calendar> getDailyEventsForMembers(@RequestParam Long careAssignmentId, @PathVariable LocalDate date) {
        return careAssignmentService.getDailyEventsForMembers(careAssignmentId, date);
    }
    // 회원별로 일정 리스트 조회 회원 아이디를
    @GetMapping("/{careAssignmentId}/events")
    public ResponseEntity<List<Calendar>> getEventsByMember(@PathVariable Long careAssignmentId) {
        // 해당 회원의 일정을 조회
        List<Calendar> calendars = calendarRepository.findCalendarByCareAssignmentId(careAssignmentId);
        return ResponseEntity.ok(calendars);
    }

    // 일정 추가
    @PostMapping("")
    public ResponseEntity<Calendar> addEvent(@RequestBody Calendar calendar) {
        try {
            if (calendar.getIsShared() == null) {
            calendar.setIsShared(false);
        }
        if (calendar.getCategory() == null || calendar.getCategory().isEmpty()) {
            calendar.setCategory("myCalendar");
        }
            Calendar savedCalendar = careAssignmentService.addEvent(calendar);
            return ResponseEntity.ok(savedCalendar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 요청 처리
        }
    }
    // 일정 수정
    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Calendar> partialUpdateEvent(@PathVariable Long eventId, @RequestBody Calendar updatedCalendar) {
        try {
            Calendar updatedEvent = careAssignmentService.updateEvent(eventId, updatedCalendar);
            if (updatedEvent != null) {
                return ResponseEntity.ok(updatedEvent);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 일정 삭제
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        try {
            careAssignmentService.deleteEvent(eventId);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 일정이 존재하지 않으면 404 반환
        }
    }
}
