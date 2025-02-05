package com.example._2024_danpoong_team_39_be.careCalendar;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import com.example._2024_danpoong_team_39_be.calendar.CalendarService;
import com.example._2024_danpoong_team_39_be.careAssignment.CareAssignmentService;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.repository.MemberRepository;
import com.example._2024_danpoong_team_39_be.login.util.JwtUtil;
import com.example._2024_danpoong_team_39_be.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/careCalendar")
public class CareCalendarController {
    @Autowired
    private CareCalendarService careCalendarService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private CareAssignmentService careAssignmentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MemberRepository memberRepository;

    //공유일정 전체 조회(weekly, daily)
    @GetMapping("/all")
    public List<Calendar> getAllSharedCareCalendarEvents() {
        return careCalendarService.getAllSharedCalendars();
    }
    //특정 일정 상세 조회
    @GetMapping("/{id}")
    public Calendar getCalendarById(@PathVariable Long id) {
        return calendarService.getCalendarById(id);
    }

    @GetMapping("/dolbomiList")
    public List<CareAssignment> getDolbomiList() {
        return careAssignmentService.getAllCareAssignments();
    }

    @GetMapping("/rest/caregiver")
    public Map<String, Object> getRestCalendarForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        return careCalendarService.getRestCalendar(date, startTime, endTime);
    }

    @GetMapping("/rest")
    public Map<String, Object> getRestCalendarForm() {
        return careCalendarService.prepareRestCalendar();
    }
    @GetMapping("/hospital/caregiver")
    public Map<String, Object> getHospitalCalendarForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        return careCalendarService.getHospiatalCalendar(date, startTime, endTime);
    }

    @GetMapping("/hospital")
    public Map<String, Object> getHospitalCalendarForm() {
       return careCalendarService.prepareHospitalCalendar();
    }
    @GetMapping("/meal/caregiver")
    public Map<String, Object> getMealCalendarForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        return careCalendarService.getMealCalendar(date, startTime, endTime);
    }

    @GetMapping("/meal")
    public Map<String, Object> getMealCalendarForm() {
        return careCalendarService.prepareMealCalendar();
    }
    @GetMapping("/others")
    public Map<String, Object> getOtherCalendarForm() {
        return careCalendarService.prepareOtherCalendar();
    }
    @GetMapping("/others/caregiver")
    public Map<String, Object> getOtherCalendarForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        return careCalendarService.getOthersCalendar(date, startTime, endTime);
    }

    @GetMapping("/medication")
    public Map<String, Object> getMedicationCalendarForm() {
        return careCalendarService.prepareMedicationCalendar();
    }
    @GetMapping("/medication/caregiver")
    public Map<String, Object> getMedicationCalendarForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        return careCalendarService.getMedicaionCalendar(date, startTime, endTime);
    }

    @PostMapping("/rest")
    public Calendar createRestCalendar(@RequestBody Calendar calendar, @RequestHeader(value = "Authorization") String token) {
        // JWT 토큰에서 이메일 추출
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String jwt = token.substring(7); // "Bearer " 이후의 실제 토큰 값
        String email = jwtUtil.getEmailFromToken(jwt); // 이메일 추출

        if (email == null) {
            throw new IllegalArgumentException("유효한 사용자를 찾을 수 없습니다.");
        }

        // 이메일을 memberId로 사용
        // Long memberId = getMemberIdFromEmail(email); // 이메일을 Long으로 변환하는 방법 (필요시)

        // careAssignmentId에 해당하는 멤버 찾기
        Long careAssignmentId = calendar.getCareAssignmentId();
        Member member = memberRepository.findByCareAssignmentId(careAssignmentId);

        if (member != null) {
            // 멤버가 존재하면 알림 전송
            notificationService.notifyCalendar(calendar);
        } else {
            // 해당 careAssignmentId에 해당하는 멤버가 없으면 알림 처리 안 함
            throw new IllegalArgumentException("해당 careAssignmentId에 해당하는 멤버를 찾을 수 없습니다.");
        }

        // 새 일정 생성 (DB에 저장)
        Calendar createdCalendar = createCalendarByCategory(calendar, "rest");

        return createdCalendar;
    }




    @PostMapping("/hospital")
    public Calendar createHospitalCalendar(@RequestBody Calendar calendar) {
        notificationService.notifyCalendar(calendar);
        return createCalendarByCategory(calendar, "hospital");
    }

    @PostMapping("/meal")
    public Calendar createMealCalendar(@RequestBody Calendar calendar) {
        notificationService.notifyCalendar(calendar);
        return createCalendarByCategory(calendar, "meal");
    }

    @PostMapping("/others")
    public Calendar createOthersCalendar(@RequestBody Calendar calendar) {
        notificationService.notifyCalendar(calendar);
        return createCalendarByCategory(calendar, "others");
    }
    private Calendar createCalendarByCategory(Calendar calendar, String category) {
        calendar.setCategory(category);
        return careCalendarService.createCalendarForAssignment(calendar.getCareAssignmentId(), calendar);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long id) {
        try {
            careCalendarService.deleteCalendar(id);
            return ResponseEntity.ok("일정이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패: " + e.getMessage());
        }


    }
    @PatchMapping("/{id}")
    public ResponseEntity<Calendar> patchCalendar(@PathVariable Long id, @RequestBody Calendar updatedCalendar) {
        try {
            // 일정 업데이트
            Calendar calendar = careCalendarService.updateCalendar(id, updatedCalendar);

            if (calendar != null) {
                return ResponseEntity.ok(calendar);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
