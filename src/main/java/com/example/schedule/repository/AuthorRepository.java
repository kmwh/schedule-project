package com.example.schedule.repository;

import com.example.schedule.entity.Author;

import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);
    Optional<Author> findByNameAndEmail(String name, String email);
    Optional<Author> findById(Long id);
}
