package com.example.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDeleteRequestDto {
    @NotBlank
    private String password;
}
