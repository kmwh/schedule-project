package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleUpdateRequestDto;
import com.example.schedule.entity.Author;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AuthorRepository authorRepository;

    // Lv1 + Lv3
    public ScheduleResponseDto create(ScheduleRequestDto request) {
        Author author = authorRepository.findByNameAndEmail(request.getAuthorName(), request.getAuthorEmail())
                .orElseGet(() -> authorRepository.save(Author.builder()
                        .name(request.getAuthorName())
                        .email(request.getAuthorEmail())
                        .build()));

        Schedule schedule = Schedule.builder()
                .todo(request.getTodo())
                .password(request.getPassword())
                .author(author)
                .build();

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    // Lv1 + Lv5
    public ScheduleResponseDto findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));
        return toResponse(schedule);
    }

    // Lv1 + Lv4
//    public Page<ScheduleResponseDto> findAll(String updatedAtStr,
//                                             String authorName,
//                                             int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
//        LocalDateTime cutoffDateTime = null;
//
//        if (StringUtils.hasText(updatedAtStr)) {
//            cutoffDateTime = LocalDate.parse(updatedAtStr).atTime(LocalTime.MAX);
//        }
//
//        Page<Schedule> schedules;
//
//        if (StringUtils.hasText(authorName) && cutoffDateTime != null) {
//            schedules = scheduleRepository.findByAuthor_NameAndUpdatedAtLessThan(authorName, cutoffDateTime, pageable);
//        } else if (StringUtils.hasText(authorName)) {
//            schedules = scheduleRepository.findByAuthor_Name(authorName, pageable);
//        } else if (cutoffDateTime != null) {
//            schedules = scheduleRepository.findByUpdatedAtLessThan(cutoffDateTime, pageable);
//        } else {
//            schedules = scheduleRepository.findAll(pageable);
//        }
//
//        return schedules.map(this::toResponse);
//    }

    // Lv1 + Lv3 + Lv4
    public Page<ScheduleResponseDto> findAll(String updatedAtStr, Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        LocalDateTime cutoffDateTime = null;

        if (StringUtils.hasText(updatedAtStr)) {
            cutoffDateTime = LocalDate.parse(updatedAtStr).atTime(LocalTime.MAX);
        }

        Page<Schedule> schedules;

        if (authorId != null && cutoffDateTime != null) {
            schedules = scheduleRepository.findByAuthor_IdAndUpdatedAtLessThanEqual(authorId, cutoffDateTime, pageable);
        } else if (authorId != null) {
            schedules = scheduleRepository.findByAuthor_Id(authorId, pageable);
        } else if (cutoffDateTime != null) {
            schedules = scheduleRepository.findByUpdatedAtLessThanEqual(cutoffDateTime, pageable);
        } else {
            schedules = scheduleRepository.findAll(pageable);
        }

        return schedules.map(this::toResponse);
    }


    // Lv2 + Lv5
    public ScheduleResponseDto update(Long id, ScheduleUpdateRequestDto request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        schedule.setTodo(request.getTodo());
        // Lv3 구현으로 주석
//        schedule.setAuthorName(request.getAuthorName());
        return toResponse(scheduleRepository.save(schedule));
    }

    // Lv2
    public void delete(Long id, String password) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        scheduleRepository.delete(schedule);
    }

    private ScheduleResponseDto toResponse(Schedule schedule) {
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