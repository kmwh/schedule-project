package com.example.schedule.repository;

import com.example.schedule.entity.Author;
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
public class AuthorRepositoryImpl implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Author> authorRowMapper = (rs, rowNum) -> {
        Author author = Author.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .build();
        author.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        author.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return author;
    };

    @Override
    public Author save(Author author) {
        LocalDateTime now = LocalDateTime.now();

        if (author.getId() == null) {
            String sql = """
                    INSERT INTO author (name, email, created_at, updated_at)
                    VALUES (?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql,
                    author.getName(),
                    author.getEmail(),
                    Timestamp.valueOf(now),
                    Timestamp.valueOf(now));

            Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            author.setId(id);
            author.setCreatedAt(now);
            author.setUpdatedAt(now);
        } else {
            String sql = """
                    UPDATE author
                       SET name = ?, email = ?, updated_at = ?
                     WHERE id = ?
                    """;
            jdbcTemplate.update(sql,
                    author.getName(),
                    author.getEmail(),
                    Timestamp.valueOf(now),
                    author.getId());
            author.setUpdatedAt(now);
        }
        return author;
    }

    @Override
    public Optional<Author> findByNameAndEmail(String name, String email) {
        String sql = "SELECT * FROM author WHERE name = ? AND email = ? LIMIT 1";
        List<Author> list = jdbcTemplate.query(sql, authorRowMapper, name, email);
        return list.stream().findFirst();
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM author WHERE id = ? LIMIT 1";
        List<Author> list = jdbcTemplate.query(sql, authorRowMapper, id);
        return list.stream().findFirst();
    }
}
