package com.example._2024_danpoong_team_39_be.careCalendar.rest;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import com.example._2024_danpoong_team_39_be.calendar.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/careCalendar/rest")
public class RestController {
    @Autowired
    private CalendarService careAssignmentService;  // Service 사용
    private Rest rest;
//    // 특정 날짜의 세부 일정 조회
//    @GetMapping("/{calendar_id}")
//    public Calendar getDailyDetailEvents( @PathVariable Long calendar_id) {
//        return calendarService.getDailyDetailEventsWithId(calendar_id);
//    }


    // 특정 날짜의 일정 추가
    @PostMapping("")
    public ResponseEntity<Calendar> addEvent(@RequestBody Calendar calendar, Principal principal) {
        try {
            // 이메일이 일치하는지 확인
            String loggedInUserEmail = principal.getName(); // 로그인한 사용자의 이메일

            if (!loggedInUserEmail.equals(calendar.getCareAssignment().getEmail())) {
                // 이메일이 일치하지 않으면 권한 없음 처리
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            if (calendar.getIsShared() == null) {
                calendar.setIsShared(true);
            }
            if (calendar.getCategory() == null || calendar.getCategory().isEmpty()) {
                calendar.setCategory("rest");
            }
            if(rest.isCaregiver() == false){
                calendar.setName("지정되지 않음");
            }
            Calendar savedCalendar = careAssignmentService.addEvent(calendar, loggedInUserEmail);
            return ResponseEntity.ok(savedCalendar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 요청 처리
        }
    }

//    // 일정 수정 (부분 수정, PATCH 사용)
//    @PatchMapping("/{calendar_id}")
//    public Calendar updateEvent(@PathVariable Long calendar_id,  @RequestBody Calendar calendar) {
//        return calendarService.updateEvent(calendar_id,  calendar);  // 서비스 사용
//    }
//
//    // 일정 삭제
//    @DeleteMapping("/{calendar_id}")
//    public boolean deleteEvent(@PathVariable Long calendar_id) {
//        return calendarService.deleteEvent(calendar_id);  // 서비스 사용
//    }
}
