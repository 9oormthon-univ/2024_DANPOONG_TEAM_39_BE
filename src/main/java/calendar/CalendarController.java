package calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;  // Service 사용


    // 특정 날짜의 일정 리스트 조회
    @GetMapping("/daily/{date}")
    public List<Calendar> getDailyEventsByDate(@PathVariable LocalDate date) {
        return calendarService.getDailyEvents(date);
    }


    // 특정 날짜의 세부 일정 조회
    @GetMapping("/daily/{date}/{calendar_id}")
    public List<Calendar> getDailyDetailEvents(@PathVariable LocalDate date, @PathVariable Long calendar_id) {
        return calendarService.getDailyDetailEvents(date, calendar_id);
    }

    // 일정 추가
    @PostMapping("/daily")
    public Calendar addEvent(@RequestBody Calendar calendar) {
        if (calendar.getIsShared() == null) {
            calendar.setIsShared(false);
        }
        return calendarService.addEvent(calendar);
    }


    // 일정 수정 (부분 수정, PATCH 사용)
    @PatchMapping("/daily/{calendar_id}")
    public Calendar updateEvent(@PathVariable Long calendar_id, @RequestBody Calendar calendar) {
        return calendarService.updateEvent(calendar_id, calendar);  // 서비스 사용
    }

    // 일정 삭제
    @DeleteMapping("/daily/{calendar_id}")
    public boolean deleteEvent(@PathVariable Long calendar_id) {
        return calendarService.deleteEvent(calendar_id);  // 서비스 사용
    }
    @GetMapping("/weekly")
    public List<Calendar> getWeeklyEvents() {
        // 오늘 날짜를 기준으로 주간 일정을 조회
        LocalDate today = LocalDate.now();
        return calendarService.getWeeklyEvents(today);
    }
    // 특정 날짜의 주간 일정 조회
    @GetMapping("/weekly/{date}")
    public List<Calendar> getWeeklyEvents(@PathVariable LocalDate date) {
        return calendarService.getWeeklyEvents(date);
    }
}
