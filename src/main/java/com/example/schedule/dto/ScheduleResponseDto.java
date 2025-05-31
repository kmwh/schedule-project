package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    private Long id;

    private String todo;

    private String authorName;

    private String authorEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ScheduleResponseDto from(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .todo(schedule.getTodo())
                .authorName(schedule.getAuthor().getName())
                .authorEmail(schedule.getAuthor().getEmail())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
