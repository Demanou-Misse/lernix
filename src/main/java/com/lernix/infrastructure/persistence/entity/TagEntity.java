package com.lernix.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tags")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TagEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;
}

