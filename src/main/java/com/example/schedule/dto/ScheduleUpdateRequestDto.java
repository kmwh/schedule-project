package com.example.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleUpdateRequestDto {
    @NotBlank
    private String todo;

    // Lv3 연관 관계 설정으로 주석
//    @NotBlank
//    private String authorName;

    @NotBlank
    private String password;
}