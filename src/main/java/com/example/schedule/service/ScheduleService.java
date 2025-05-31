package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.ScheduleUpdateRequestDto;
import com.example.schedule.entity.Author;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public ScheduleResponseDto create(ScheduleRequestDto request) {

        Author author = authorRepository
                .findByNameAndEmail(request.getAuthorName(), request.getAuthorEmail())
                .orElseGet(() -> authorRepository.save(
                        Author.builder()
                                .name(request.getAuthorName())
                                .email(request.getAuthorEmail())
                                .build()
                ));

        Schedule schedule = Schedule.builder()
                .todo(request.getTodo())
                .password(request.getPassword())
                .author(author)
                .build();

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    public ScheduleResponseDto findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        return toResponse(schedule);
    }

    public List<ScheduleResponseDto> findAll(String updatedAt,
                                             Long authorId,
                                             int page,
                                             int size) {

        LocalDateTime cutoff = null;
        if (StringUtils.hasText(updatedAt)) {
            cutoff = LocalDate.parse(updatedAt).atTime(LocalTime.MAX);
        }

        List<Schedule> schedules;

        if (authorId != null && cutoff != null) {
            schedules = scheduleRepository
                    .findByAuthorIdAndUpdatedAtLessThanEqual(authorId, cutoff, page, size);
        } else if (authorId != null) {
            schedules = scheduleRepository
                    .findByAuthorId(authorId, page, size);
        } else if (cutoff != null) {
            schedules = scheduleRepository
                    .findByUpdatedAtLessThanEqual(cutoff, page, size);
        } else {
            schedules = scheduleRepository.findAll(page, size);
        }

        return schedules.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponseDto update(Long id, ScheduleUpdateRequestDto request) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        schedule.setTodo(request.getTodo());
        Schedule updated = scheduleRepository.save(schedule);   // UPDATE 분기 처리
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id, String password) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if (!scheduleRepository.delete(id)) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패했습니다.");
        }
    }

    private ScheduleResponseDto toResponse(Schedule schedule) {
        return ScheduleResponseDto.from(schedule);
    }
}
