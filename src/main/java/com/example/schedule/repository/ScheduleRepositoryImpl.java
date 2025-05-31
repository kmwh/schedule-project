package com.example.schedule.repository;

import com.example.schedule.entity.Author;
import com.example.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Schedule> scheduleRowMapper = (rs, rowNum) -> {
        Author author = Author.builder()
                .id(rs.getLong("author_id"))
                .name(rs.getString("author_name"))
                .email(rs.getString("author_email"))
                .build();

        Schedule schedule = Schedule.builder()
                .id(rs.getLong("id"))
                .todo(rs.getString("todo"))
                .password(rs.getString("password"))
                .author(author)
                .build();

        schedule.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        schedule.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return schedule;
    };

    @Override
    public Schedule save(Schedule schedule) {
        LocalDateTime now = LocalDateTime.now();

        if (schedule.getId() == null) {
            schedule.setCreatedAt(now);
            schedule.setUpdatedAt(now);

            String sql = """
                    INSERT INTO schedule (todo, password, created_at, updated_at, author_id)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql,
                    schedule.getTodo(),
                    schedule.getPassword(),
                    Timestamp.valueOf(schedule.getCreatedAt()),
                    Timestamp.valueOf(schedule.getUpdatedAt()),
                    schedule.getAuthor().getId());

            Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            schedule.setId(id);
        } else {
            schedule.setUpdatedAt(now);

            String sql = """
                    UPDATE schedule
                       SET todo = ?, password = ?, updated_at = ?
                     WHERE id = ?
                    """;
            jdbcTemplate.update(sql,
                    schedule.getTodo(),
                    schedule.getPassword(),
                    Timestamp.valueOf(schedule.getUpdatedAt()),
                    schedule.getId());
        }
        return schedule;
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        String sql = baseSelect() + " WHERE s.id = ?";
        List<Schedule> list = jdbcTemplate.query(sql, scheduleRowMapper, id);
        return list.stream().findFirst();
    }

    @Override
    public List<Schedule> findAll(int page, int size) {
        int offset = page * size;
        String sql = baseSelect() +
                " ORDER BY s.updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, scheduleRowMapper, size, offset);
    }

    @Override
    public List<Schedule> findByAuthorIdAndUpdatedAtLessThanEqual(
            Long authorId, LocalDateTime updatedAt, int page, int size) {

        int offset = page * size;
        String sql = baseSelect() +
                " WHERE s.author_id = ? AND s.updated_at <= ? " +
                " ORDER BY s.updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, scheduleRowMapper,
                authorId, Timestamp.valueOf(updatedAt), size, offset);
    }

    @Override
    public List<Schedule> findByAuthorId(Long authorId, int page, int size) {
        int offset = page * size;
        String sql = baseSelect() +
                " WHERE s.author_id = ? ORDER BY s.updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, scheduleRowMapper, authorId, size, offset);
    }

    @Override
    public List<Schedule> findByUpdatedAtLessThanEqual(
            LocalDateTime updatedAt, int page, int size) {

        int offset = page * size;
        String sql = baseSelect() +
                " WHERE s.updated_at <= ? ORDER BY s.updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, scheduleRowMapper,
                Timestamp.valueOf(updatedAt), size, offset);
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private String baseSelect() {
        return """
               SELECT s.id, s.todo, s.password,
                      s.created_at, s.updated_at,
                      a.id   AS author_id,
                      a.name AS author_name,
                      a.email AS author_email
               FROM schedule s
               JOIN author a ON s.author_id = a.id
               """;
    }
}
