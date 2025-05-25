package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleUpdateRequestDto;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    // Lv1 + Lv6
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> create(@Validated @RequestBody ScheduleRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.create(request));
    }

    // Lv1
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    // Lv1 + Lv3(id로 찾는 기능 추가) + Lv4(페이지네이션 추가)
    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto>> findAll(
            @RequestParam(required = false) String updatedAt,
            //@RequestParam(required = false) String authorName,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(scheduleService.findAll(updatedAt, authorName, page, size));
        return ResponseEntity.ok(scheduleService.findAll(updatedAt, authorId, page, size));
    }

    // Lv2 + Lv6
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> update(
            @PathVariable Long id,
            @Validated @RequestBody ScheduleUpdateRequestDto request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    // Lv2
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam String password) {
        scheduleService.delete(id, password);
        return ResponseEntity.noContent().build();
    }
}