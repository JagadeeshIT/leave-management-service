package com.company.leave.leave;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    private String name;
    private String department;

    @Enumerated(EnumType.STRING)
    private Role role;
}
