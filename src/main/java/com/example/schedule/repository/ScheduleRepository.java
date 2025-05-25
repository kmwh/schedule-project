package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByAuthor_Name(String name, Pageable pageable);
    Page<Schedule> findByAuthor_NameAndUpdatedAtLessThan(String name,
                                                         LocalDateTime updatedAt,
                                                         Pageable pageable);
    Page<Schedule> findByUpdatedAtLessThan(LocalDateTime updatedAt, Pageable pageable);
    Page<Schedule> findByAuthor_IdAndUpdatedAtLessThanEqual(Long authorId, LocalDateTime updatedAt, Pageable pageable);
    Page<Schedule> findByAuthor_Id(Long authorId, Pageable pageable);
    Page<Schedule> findByUpdatedAtLessThanEqual(LocalDateTime updatedAt, Pageable pageable);


}