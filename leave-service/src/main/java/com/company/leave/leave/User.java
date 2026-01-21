package com.company.leave.leave;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private Long id;   // same as employeeId

    private String name;

    private String department;

    @Enumerated(EnumType.STRING)
    private Role role;

    //private Long managerId; 

}
