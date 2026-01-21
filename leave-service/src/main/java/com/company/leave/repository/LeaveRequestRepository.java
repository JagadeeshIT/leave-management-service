package com.company.leave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.leave.leave.LeaveRequest;
import com.company.leave.leave.LeaveStatus;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

	List<LeaveRequest> findByEmployeeId(Long employeeId);

	List<LeaveRequest> findByStatus(LeaveStatus pending);

}
