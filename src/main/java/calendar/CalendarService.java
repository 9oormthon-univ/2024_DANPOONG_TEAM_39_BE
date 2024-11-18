package calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    // 특정 날짜의 일정 조회
    public List<Calendar> getDailyEvents(LocalDate date) {
        return calendarRepository.findByDate(date);
    }

    // 특정 날짜의 세부 일정 조회
    public List<Calendar> getDailyDetailEvents(LocalDate date, Long id) {
        return calendarRepository.findByDateAndId(date, id);
    }
    public Calendar getDailyDetailEventsWithId(Long id) {
        // id로 Calendar를 가져옵니다.
        return calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 일정을 찾을 수 없습니다."));
    }
    // 일정 추가
    public Calendar addEvent(Calendar calendar) {
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
                repeatEvent.setTitle(calendar.getTitle());
                repeatEvent.setRepeatCycle(calendar.getRepeatCycle());
                repeatEvent.setIsShared(calendar.getIsShared());
                repeatEvent.setCategory(calendar.getCategory());
                repeatEvent.setMeal(calendar.getMeal());
                repeatEvent.setHospital(calendar.getHospital());
                repeatEvent.setRest(calendar.getRest());
                repeatEvent.setMedication(calendar.getMedication());

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

    // 반복 주기에 따라 날짜 리스트 생성
    private List<LocalDate> generateRepeatDates(LocalDate startDate, Calendar.RepeatCycle repeatCycle) {
        List<LocalDate> repeatDates = new ArrayList<>();
        switch (repeatCycle) {
            case WEEKLY:
                for (int i = 0; i < 7; i++) { // 매주 반복되는 날짜 추가
                    repeatDates.add(startDate.plusWeeks(i)); // startDate로부터 1주일 간격으로 날짜 추가
                }
                break;
            case DAILY:
                repeatDates.add(startDate); // 하루 일정만 추가
                break;
            case MONTHLY:
                for (int i = 0; i < 30; i++) { // 한 달치 날짜 추가
                    repeatDates.add(startDate.plusDays(i));
                }
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 반복 주기입니다.");
        }
        return repeatDates;
    }



    // 공유 일정의 필드를 검증
    private void validateSharedEventFields(Calendar calendar) {
        if (calendar.getCategory() == null) {
            throw new IllegalArgumentException("공유 일정일 경우 카테고리를 선택해야 합니다.");
        }

        // 카테고리별 필드 체크
        switch (calendar.getCategory()) {
            case "Meal":
                if (calendar.getMeal() == null || calendar.getMeal().getMealType() == null) {
                    throw new IllegalArgumentException("Meal 카테고리에는 mealType을 지정해야 합니다.");
                }
                break;
            case "Hospital":
                if (calendar.getHospital() == null || calendar.getHospital().getTransportationTpye() == null) {
                    throw new IllegalArgumentException("Hospital 카테고리에는 transportationType을 지정해야 합니다.");
                }
                break;
            case "Rest":
                if (calendar.getRest() == null || calendar.getRest().getRestType() == null) {
                    throw new IllegalArgumentException("Rest 카테고리에는 restType을 지정해야 합니다.");
                }
                break;
            case "Medication":
                if (calendar.getMedication() == null || calendar.getMedication().getMedicationType() == null) {
                    throw new IllegalArgumentException("Medication 카테고리에는 medicationType을 지정해야 합니다.");
                }
                break;
            case "others":
                // "others" 카테고리는 제약이 없으므로 추가적인 검증은 없음
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }
    }

    // 공유 일정 관련 필드 초기화
    private void resetSharedEventFields(Calendar calendar) {
        calendar.setCategory(null);
        calendar.setMeal(null);
        calendar.setHospital(null);
        calendar.setRest(null);
        calendar.setMedication(null);
    }



    // 세부 일정 수정 (부분 수정, PATCH 요청 사용)
    public Calendar updateEvent(Long id, Calendar updatedCalendar) {
        Optional<Calendar> existingCalendar = calendarRepository.findById(id);
        if (existingCalendar.isPresent()) {
            Calendar calendar = existingCalendar.get();

            // 날짜가 일치하는 경우에만 수정
            if (calendar.getId().equals(id)) {
                // 기존 일정에서 변경된 필드만 업데이트
                if (updatedCalendar.getTitle() != null) {
                    calendar.setTitle(updatedCalendar.getTitle());
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
                if (updatedCalendar.getRepeatCycle() != null) {
                    // 반복 주기가 설정된 경우
                    LocalDate startDate = updatedCalendar.getDate();

                    if (startDate == null) {
                        throw new IllegalArgumentException("반복 일정은 startDate가 필요합니다.");
                    }

                    // 반복 주기에 따른 일정 생성
                    switch (updatedCalendar.getRepeatCycle()) {
                        case WEEKLY:
                            List<LocalDate> repeatDates = new ArrayList<>();
                            for (int i = 0; i < 7; i++) {
                                repeatDates.add(startDate.plusDays(i));
                            }
                            System.out.println("반복 일정 (주간): " + repeatDates);
                            break;
                        case DAILY:
                            System.out.println("반복 일정 (일간): " + startDate);
                            break;
                        case MONTHLY:
                            List<LocalDate> monthlyDates = new ArrayList<>();
                            for (int i = 0; i < 30; i++) {
                                monthlyDates.add(startDate.plusDays(i));
                            }
                            System.out.println("반복 일정 (월간): " + monthlyDates);
                            break;
                        default:
                            throw new IllegalArgumentException("지원되지 않는 반복 주기입니다.");
                    }
                } else {
                    // 반복 주기가 없는 경우, startDate만 설정
                    LocalDate today = LocalDate.now();
                    calendar.setDate(today);
                    System.out.println("단일 일정: " + today);
                }

                if (updatedCalendar.getIsAlarm() != null) {
                    calendar.setIsAlarm(updatedCalendar.getIsAlarm());
                }
                if (updatedCalendar.getIsShared() != null) {
                    calendar.setIsShared(updatedCalendar.getIsShared());

                    // isShared가 true일 경우 카테고리 값 체크
                    if (updatedCalendar.getIsShared()) {
                        if (updatedCalendar.getCategory() == null) {
                            throw new IllegalArgumentException("공유 일정일 경우 카테고리를 선택해야 합니다.");
                        }

                        // 카테고리별 추가 필드 체크
                        switch (updatedCalendar.getCategory()) {
                            case "Meal":
                                if (updatedCalendar.getMeal() == null || updatedCalendar.getMeal().getMealType() == null) {
                                    throw new IllegalArgumentException("Meal 카테고리일 경우 mealType을 선택해야 합니다.");
                                }
                                break;
                            case "Hospital":
                                if (updatedCalendar.getHospital() == null || updatedCalendar.getHospital().getTransportationTpye() == null) {
                                    throw new IllegalArgumentException("Hospital 카테고리일 경우 transportationMethod을 선택해야 합니다.");
                                }
                                break;
                            case "Rest":
                                if (updatedCalendar.getRest() == null || updatedCalendar.getRest().getRestType() == null) {
                                    throw new IllegalArgumentException("Rest 카테고리일 경우 restType을 선택해야 합니다.");
                                }
                                break;
                            case "Medication":
                                if (updatedCalendar.getMedication() == null || updatedCalendar.getMedication().getMedicationType() == null) {
                                    throw new IllegalArgumentException("Medication 카테고리일 경우 medicationType을 선택해야 합니다.");
                                }
                                break;
                            case "others":
                                // "others" 카테고리는 제약이 없으므로 추가적인 검증은 없음
                                break;
                            default:
                                throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
                        }
                    } else {
                        // isShared가 false일 경우, 카테고리 값은 받지 않음
                        calendar.setCategory(null);  // 카테고리 초기화
                    }
                }

                // 카테고리와 관련된 필드 값 업데이트
                if (updatedCalendar.getCategory() != null) {
                    calendar.setCategory(updatedCalendar.getCategory());
                }

                // 변경된 일정 저장
                calendarRepository.save(calendar);
                return calendar;
            }
        }
        return null;
    }



    // 세부 일정 삭제
    public boolean deleteEvent(Long id) {
        Optional<Calendar> existingCalendar = calendarRepository.findById(id);
        if (existingCalendar.isPresent()) {
            Calendar calendar = existingCalendar.get();

            // 날짜가 일치하는 경우에만 삭제
            if (calendar.getId().equals(id)) {
                calendarRepository.deleteById(id);
                return true;  // 삭제 성공
            }
        }
        return false;  // 삭제할 일정이 없으면 false 반환
    }

    // 특정 날짜의 주간 일정 조회
    public List<Calendar> getWeeklyEvents(LocalDate date) {
        // 해당 날짜가 속한 주의 일요일을 구함
        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);

        // 해당 주의 일요일부터 토요일까지의 날짜 생성
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        // 해당 주간의 일정을 조회
        return calendarRepository.findByDateIn(weekDates);
    }

}
