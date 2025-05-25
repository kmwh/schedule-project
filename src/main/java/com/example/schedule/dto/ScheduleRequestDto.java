package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDto {
    @NotBlank
    @Size(max = 200)
    private String todo;

    @NotBlank
    private String password;

    @NotBlank
    private String authorName;

    @Email
    @NotBlank
    private String authorEmail;
}
