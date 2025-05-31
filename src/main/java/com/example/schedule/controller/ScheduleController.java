package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleDeleteRequestDto;
import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleUpdateRequestDto;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> create(
            @Validated @RequestBody ScheduleRequestDto request) {

        ScheduleResponseDto response = scheduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ScheduleResponseDto> responses =
                scheduleService.findAll(updatedAt, authorId, page, size);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> update(
            @PathVariable Long id,
            @Validated @RequestBody ScheduleUpdateRequestDto request) {

        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @Validated @RequestBody ScheduleDeleteRequestDto request) {

        scheduleService.delete(id, request.getPassword());
        return ResponseEntity.noContent().build();
    }
}
