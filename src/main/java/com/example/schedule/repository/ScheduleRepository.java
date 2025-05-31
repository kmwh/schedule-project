package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    Optional<Schedule> findById(Long id);

    List<Schedule> findAll(int page, int size);

    boolean delete(Long id);

    List<Schedule> findByAuthorIdAndUpdatedAtLessThanEqual(
            Long authorId, LocalDateTime updatedAt, int page, int size);

    List<Schedule> findByAuthorId(Long authorId, int page, int size);

    List<Schedule> findByUpdatedAtLessThanEqual(
            LocalDateTime updatedAt, int page, int size);
}
