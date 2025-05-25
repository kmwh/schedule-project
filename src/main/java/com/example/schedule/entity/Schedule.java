package com.example.schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200) // Lv6
    private String todo;

    @NotBlank // Lv6
    private String password;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // Lv3 구현으로 주석
//    @NotBlank
//    private String authorName;
}