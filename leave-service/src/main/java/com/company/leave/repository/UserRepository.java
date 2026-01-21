package com.company.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.leave.leave.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

