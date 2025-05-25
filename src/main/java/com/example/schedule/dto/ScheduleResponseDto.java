package com.example.schedule.dto;

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
}
