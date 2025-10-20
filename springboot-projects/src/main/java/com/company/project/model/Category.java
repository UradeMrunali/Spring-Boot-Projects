package com.company.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private UUID categoryId;
    private String name;
    private UUID userId;
    private LocalDateTime createdAt;
}
