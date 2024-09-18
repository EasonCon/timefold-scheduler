package base_domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Resource extends Labeled {
    private String name;
    private List<TimeSlot> timeSlots;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    private LocalDateTime localDateTime = null;

    public Resource(String id, String name, List<TimeSlot> timeSlots) {
        super(id);
        this.name = name;
        this.timeSlots = timeSlots;
    }

    public Resource() {
        super(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public void generateTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();

        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        // 从9月1日到12月31日的每一天
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

            timeSlots.add(new TimeSlot(startDateTime, endDateTime));
        }
        this.setTimeSlots(timeSlots);
        this.setLocalDateTime(timeSlots.getFirst().getStart());

    }

    public LocalDateTime getValidStartTime(LocalDateTime possibleStartTime) {
        // 传入一个时间，返回合法开始时间 TODO: 边界处理
        for (TimeSlot timeSlot : this.getTimeSlots()) {
            if (timeSlot.getEnd().isBefore(possibleStartTime)) {
                continue;
            }
            if (timeSlot.getStart().isBefore(possibleStartTime) && timeSlot.getEnd().isAfter(possibleStartTime)) {
                return possibleStartTime;
            }
            if (timeSlot.getStart().isAfter(possibleStartTime)) {
                return timeSlot.getStart();
            }
        }
        return null;
    }

    public LocalDateTime getValidEndTime(LocalDateTime startTime, Duration duration) {
        if (startTime == null || startTime.isAfter(this.getTimeSlots().getLast().getEnd())) {
            return null;
        }

        Duration remainingDuration = duration; // 剩余的持续时间

        for (TimeSlot timeSlot : this.getTimeSlots()) {
            if (timeSlot.getEnd().isBefore(startTime)) {
                continue;
            }
            if (startTime.isBefore(timeSlot.getEnd()) && startTime.isAfter(timeSlot.getStart())) {
                Duration availableDurationInSlot = Duration.between(startTime, timeSlot.getEnd());

                if (!availableDurationInSlot.minus(remainingDuration).isNegative()) {
                    return startTime.plus(remainingDuration);
                } else {
                    remainingDuration = remainingDuration.minus(availableDurationInSlot);
                    startTime = timeSlot.getEnd(); // 将 startTime 设置为下一个时间段的开始
                }
            } else if (startTime.isBefore(timeSlot.getStart())) {
                Duration availableDurationInSlot = Duration.between(timeSlot.getStart(), timeSlot.getEnd());

                if (!availableDurationInSlot.minus(remainingDuration).isNegative()) {
                    return timeSlot.getStart().plus(remainingDuration); // 计算结束时间
                } else {
                    remainingDuration = remainingDuration.minus(availableDurationInSlot);
                    startTime = timeSlot.getEnd(); // 更新 startTime
                }
            }
        }

        // 如果所有时间段都不足以容纳持续时间，返回 null
        return null;
    }


}
