package com.example._2024_danpoong_team_39_be.careCalendar;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/careCalendar")
public class CareCalendarController {
    @Autowired
    private CareCalendarService careCalendarService;

    @GetMapping("/all")
    public List<Calendar> getAllSharedCareCalendarEvents() {
        return careCalendarService.getAllSharedCalendars();
    }
    //일일 공유 일정
    @GetMapping("/{date}")
    public List<Calendar> getCareEventsByDate(@PathVariable LocalDate date) {
        return careCalendarService.getCareCalendarEventsByDate(date);
    }

//    @GetMapping("/weekly/{date}")
//    public List<Calendar> getWeeklySharedEvents(@PathVariable LocalDate date) {
//        return careCalendarService.getWeeklyCareCalendarEvents(date);
//    }

//    @GetMapping("/weekly")
//    public List<Calendar> getWeeklySharedToday(@RequestParam(value = "date", required = false) LocalDate date) {
//        if (date == null) {
//            date = LocalDate.now();
//        }
//        return careCalendarService.getWeeklyCareCalendarEvents(date);
//    }

}
