package com.example._2024_danpoong_team_39_be.calendar;

import com.example._2024_danpoong_team_39_be.careAssignment.CareAssignmentRepository;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.example._2024_danpoong_team_39_be.domain.CareRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CareAssignmentRepository careAssignmentRepository;
    // careAssignmentId로 해당 CareAssignment 객체 조회
// 특정 날짜의 세부 일정 조회
    public List<Calendar> getDailyDetailEvents(LocalDate date, Long id) {
        return calendarRepository.findByDateAndId(date, id);
    }
    // 특정 날짜의 일정 조회
    public List<Calendar> getDailyEventsForMembers(Long id, LocalDate date) {
        return calendarRepository.findCalendarByCareAssignmentIdAndDate(id, date);
    }

    // 일정 추가
    public Calendar addEvent(Calendar calendar) {
        // CareAssignment가 존재하는지 확인


        // 시간 충돌 검사
        if (isTimeConflict(calendar)) {
            throw new IllegalArgumentException("해당 시간대에 이미 일정이 존재합니다.");
        }
        // 종일선택시 starttime  수면시작시간 , endTime - 수면종료시간

        if (Boolean.TRUE.equals(calendar.getIsAllday())) {
            calendar.setStartTime(calendar.getCareAssignment().getRecipient().getStartSleepTime());
            calendar.setEndTime(calendar.getCareAssignment().getRecipient().getEndSleepTime());
        }

        // 주기 설정에 따라 일정 시작 날짜를 자동으로 생성
        if (calendar.getRepeatCycle() != null) {
            LocalDate startDate = calendar.getDate(); // startDate 가져오기

            if (startDate == null) {
                throw new IllegalArgumentException("반복 일정은 시작 날짜(startDate)가 필요합니다.");
            }

            // 반복 주기에 따라 일정 리스트 생성
            List<LocalDate> repeatDates = generateRepeatDates(startDate, calendar.getRepeatCycle());

            // 반복 일정 저장
            for (LocalDate repeatDate : repeatDates) {
                Calendar repeatEvent = new Calendar();
                repeatEvent.setDate(repeatDate);
                repeatEvent.setName(calendar.getName());
                repeatEvent.setEventType(calendar.getEventType());
                repeatEvent.setStartTime(calendar.getStartTime());
                repeatEvent.setEndTime(calendar.getEndTime());
                repeatEvent.setName(calendar.getCareAssignment().getMember().getAlias());
                repeatEvent.setTitle(calendar.getTitle());
                repeatEvent.setIsAllday(calendar.getIsAllday());
                repeatEvent.setMemo(calendar.getMemo());
                repeatEvent.setLocation(calendar.getLocation());
                repeatEvent.setRepeatCycle(calendar.getRepeatCycle());
                repeatEvent.setIsShared(calendar.getIsShared());
                repeatEvent.setCategory(calendar.getCategory());
                repeatEvent.setIsAllday(calendar.getIsAllday());
                repeatEvent.setMeal(calendar.getMeal());
                repeatEvent.setHospital(calendar.getHospital());
                repeatEvent.setRest(calendar.getRest());
                repeatEvent.setMedication(calendar.getMedication());



                // 시간 충돌 검사
                if (isTimeConflict(repeatEvent)) {
                    throw new IllegalArgumentException("반복 일정 중 일부가 이미 존재하는 일정과 겹칩니다.");
                }

                // 반복 일정 저장
                calendarRepository.save(repeatEvent);
            }

            // 반복 일정 확인
            System.out.println("반복 일정: " + repeatDates);
        } else {
            // 반복 주기가 없을 경우, 시작 날짜만 설정
            if (calendar.getDate() == null) {
                calendar.setDate(LocalDate.now());
            }


            // 일정 저장
            calendarRepository.save(calendar);
        }

        // isShared가 true일 때 카테고리와 관련된 필드 체크
        if (Boolean.TRUE.equals(calendar.getIsShared())) {
            validateSharedEventFields(calendar);
        } else {
            // isShared가 false일 경우 관련 필드들을 null로 초기화
            resetSharedEventFields(calendar);
        }

        return calendar;  // 추가된 일정 객체를 반환
    }

    // 시간 충돌 검사 메서드
    private boolean isTimeConflict(Calendar calendar) {
        LocalTime adjustedStartTime = calendar.getStartTime();
        LocalTime adjustedEndTime = calendar.getEndTime();

        List<Calendar> conflictingEvents = calendarRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThan(
                calendar.getDate(), adjustedEndTime, adjustedStartTime);
        return !conflictingEvents.isEmpty();
    }


    // 반복 주기에 따라 날짜 리스트 생성
    private List<LocalDate> generateRepeatDates(LocalDate startDate, Calendar.RepeatCycle repeatCycle) {
        List<LocalDate> repeatDates = new ArrayList<>();
        switch (repeatCycle) {
            case WEEKLY:
                for (int i = 1; i <= 7; i++) {
                    repeatDates.add(startDate.plusDays(i - 1));
                }
                break;
            case DAILY:
                repeatDates.add(startDate);
                break;
            case MONTHLY:
                for (int i = 0; i < 30; i++) {
                    repeatDates.add(startDate.plusDays(i));
                }
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 반복 주기입니다.");
        }
        return repeatDates;
    }

    private void validateSharedEventFields(Calendar calendar) {
        if (calendar.getCategory() == null) {
            throw new IllegalArgumentException("공유 일정일 경우 카테고리를 선택해야 합니다.");
        }

        switch (calendar.getCategory()) {
            case "meal":
                if (calendar.getMeal() == null || calendar.getMeal().getMealType() == null) {
                    throw new IllegalArgumentException("Meal 카테고리에는 mealType을 지정해야 합니다.");
                }
                break;
            case "hospital":
                if (calendar.getHospital() == null || calendar.getHospital().getTransportationTpye() == null) {
                    throw new IllegalArgumentException("Hospital 카테고리에는 transportationType을 지정해야 합니다.");
                }
                break;
            case "rest":
                if (calendar.getRest() == null || calendar.getRest().getRestType() == null) {
                    throw new IllegalArgumentException("Rest 카테고리에는 restType을 지정해야 합니다.");
                }
                break;
            case "medication":
                if (calendar.getMedication() == null || calendar.getMedication().getMedicationType() == null) {
                    throw new IllegalArgumentException("Medication 카테고리에는 medicationType을 지정해야 합니다.");
                }
                break;
            case "others":
                break;
            case "myCalendar":
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }
    }

    private void resetSharedEventFields(Calendar calendar) {
        calendar.setCategory(null);
        calendar.setMeal(null);
        calendar.setHospital(null);
        calendar.setRest(null);
        calendar.setMedication(null);
    }

    // 일정 수정
    public Calendar updateEvent(Long id, Calendar updatedCalendar) {
        Optional<Calendar> existingCalendar = calendarRepository.findById(id);
        if (existingCalendar.isPresent()) {
            Calendar calendar = existingCalendar.get();

            if (calendar.getId().equals(id)) {
                if (isTimeConflict(updatedCalendar)) {
                    throw new IllegalArgumentException("해당 시간대에 이미 일정이 존재합니다.");
                }

                if (updatedCalendar.getTitle() != null) {
                    calendar.setTitle(updatedCalendar.getTitle());
                }
                if (updatedCalendar.getEventType() != null) {
                    calendar.setEventType(updatedCalendar.getEventType());
                }
                if (updatedCalendar.getStartTime() != null) {
                    calendar.setStartTime(updatedCalendar.getStartTime());
                }
                if (updatedCalendar.getEndTime() != null) {
                    calendar.setEndTime(updatedCalendar.getEndTime());
                }
                if (updatedCalendar.getLocation() != null) {
                    calendar.setLocation(updatedCalendar.getLocation());
                }
                if (updatedCalendar.getMemo() != null) {
                    calendar.setMemo(updatedCalendar.getMemo());
                }
                if(updatedCalendar.getIsAllday() != null){
                    calendar.setIsAllday(updatedCalendar.getIsAllday());
                }
                if (updatedCalendar.getRepeatCycle() != null) {
                    LocalDate startDate = updatedCalendar.getDate();

                    if (startDate == null) {
                        throw new IllegalArgumentException("반복 일정은 startDate가 필요합니다.");
                    }

                    switch (updatedCalendar.getRepeatCycle()) {
                        case WEEKLY:
                            List<LocalDate> repeatDates = new ArrayList<>();
                            for (int i = 0; i < 7; i++) {
                                repeatDates.add(startDate.plusDays(i));
                            }
                            break;
                        case DAILY:
                            break;
                        case MONTHLY:
                            List<LocalDate> monthlyDates = new ArrayList<>();
                            for (int i = 0; i < 30; i++) {
                                monthlyDates.add(startDate.plusDays(i));
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("지원되지 않는 반복 주기입니다.");
                    }
                } else {
                    LocalDate today = LocalDate.now();
                    calendar.setDate(today);
                }

                if (updatedCalendar.getIsAlarm() != null) {
                    calendar.setIsAlarm(updatedCalendar.getIsAlarm());
                }
                if (updatedCalendar.getIsShared() != null) {
                    calendar.setIsShared(updatedCalendar.getIsShared());

                    if (updatedCalendar.getIsShared()) {
                        if (updatedCalendar.getCategory() == null) {
                            throw new IllegalArgumentException("공유 일정일 경우 카테고리를 선택해야 합니다.");
                        }

                        validateSharedEventFields(updatedCalendar);
                    } else {
                        resetSharedEventFields(updatedCalendar);
                    }
                }

                return calendarRepository.save(calendar);
            }
        }
        return null;
    }

    // 일정 삭제
    public void deleteEvent(Long id) {
        Optional<Calendar> calendar = calendarRepository.findById(id);
        if (calendar.isPresent()) {
            calendarRepository.delete(calendar.get());
        } else {
            throw new IllegalArgumentException("일정을 찾을 수 없습니다.");
        }
    }
}
