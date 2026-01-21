package com.company.leave.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.leave.dto.ApplyLeaveRequest;
import com.company.leave.dto.LeaveResponse;
import com.company.leave.leave.LeaveRequest;
import com.company.leave.leave.User;
import com.company.leave.repository.UserRepository;
import com.company.leave.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/leaves")
public class EmployeeLeaveController {

    private final LeaveService leaveService;
	private final UserRepository userRepository;


    public EmployeeLeaveController(LeaveService leaveService, UserRepository userRepository) {
        this.leaveService = leaveService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(
            @RequestParam Long employeeId,   // TEMP: will come from JWT later
            @Valid @RequestBody ApplyLeaveRequest request) {

        // 1. Call service (service only saves leave)
        LeaveRequest savedLeave =
                leaveService.applyLeave(employeeId, request);

        // 2. Fetch user details for response
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Build response DTO
        LeaveResponse response = new LeaveResponse();
        response.setLeaveId(savedLeave.getId());
        response.setEmployee_id(employeeId);
        response.setEmployeeName(user.getName());
        response.setStartDate(savedLeave.getStartDate());
        response.setEndDate(savedLeave.getEndDate());
        response.setStatus(savedLeave.getStatus());

        response.setNumberOfDays(
                ChronoUnit.DAYS.between(
                        savedLeave.getStartDate(),
                        savedLeave.getEndDate()
                ) + 1
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    
    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(
            @RequestParam Long employeeId) {   // TEMP: from JWT later

        List<LeaveRequest> leaves =
                leaveService.getLeavesByEmployee(employeeId);

        // Fetch user once
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LeaveResponse> response = leaves.stream().map(leave -> {
            LeaveResponse dto = new LeaveResponse();
            dto.setLeaveId(leave.getId());
            dto.setEmployee_id(employeeId);
            dto.setEmployeeName(user.getName());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setStatus(leave.getStatus());

            dto.setNumberOfDays(
                    ChronoUnit.DAYS.between(
                            leave.getStartDate(),
                            leave.getEndDate()
                    ) + 1
            );

            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    
    @PutMapping("/{leaveId}/cancel")
    public ResponseEntity<LeaveResponse> cancelLeave(
            @RequestParam Long employeeId,   // TEMP: from JWT later
            @PathVariable Long leaveId) {

        LeaveRequest cancelledLeave =
                leaveService.cancelLeave(employeeId, leaveId);

        // Fetch user
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveResponse response = new LeaveResponse();
        response.setLeaveId(cancelledLeave.getId());
        response.setEmployee_id(employeeId);
        response.setEmployeeName(user.getName());
        response.setStartDate(cancelledLeave.getStartDate());
        response.setEndDate(cancelledLeave.getEndDate());
        response.setStatus(cancelledLeave.getStatus());

        response.setNumberOfDays(
                ChronoUnit.DAYS.between(
                        cancelledLeave.getStartDate(),
                        cancelledLeave.getEndDate()
                ) + 1
        );

        return ResponseEntity.ok(response);
    }



    
}
