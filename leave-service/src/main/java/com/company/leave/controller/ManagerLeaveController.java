package com.company.leave.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.leave.dto.LeaveResponse;
import com.company.leave.leave.LeaveRequest;
import com.company.leave.leave.User;
import com.company.leave.repository.UserRepository;
import com.company.leave.service.LeaveService;

@RestController
@RequestMapping("/manager/leaves")
public class ManagerLeaveController {

    private final LeaveService leaveService;
	private final UserRepository userRepository;

    public ManagerLeaveController(LeaveService leaveService, UserRepository userRepository) {
        this.leaveService = leaveService;
        this.userRepository = userRepository;
    }
    
    
    @GetMapping("/pending")
    public ResponseEntity<List<LeaveResponse>> getPendingLeaves() {

        List<LeaveRequest> leaves = leaveService.getPendingLeaves();

        // Fetch all users once (efficient)
        Map<Long, String> employeeNameMap =
                userRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                User::getId,
                                User::getName
                        ));

        List<LeaveResponse> response = leaves.stream().map(leave -> {
            LeaveResponse dto = new LeaveResponse();
            dto.setLeaveId(leave.getId());
            dto.setEmployee_id(leave.getEmployeeId());
            dto.setEmployeeName(employeeNameMap.get(leave.getEmployeeId()));
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


    // APPROVE LEAVE

    @PutMapping("/{leaveId}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable Long leaveId) {

        LeaveRequest approvedLeave =
                leaveService.approveLeave(leaveId);

        User user = userRepository.findById(approvedLeave.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveResponse response = new LeaveResponse();
        response.setLeaveId(approvedLeave.getId());
        response.setEmployee_id(approvedLeave.getEmployeeId());
        response.setEmployeeName(user.getName());
        response.setStartDate(approvedLeave.getStartDate());
        response.setEndDate(approvedLeave.getEndDate());
        response.setStatus(approvedLeave.getStatus());

        response.setNumberOfDays(
                ChronoUnit.DAYS.between(
                        approvedLeave.getStartDate(),
                        approvedLeave.getEndDate()
                ) + 1
        );

        return ResponseEntity.ok(response);
    }

    // REJECT LEAVE

    @PutMapping("/{leaveId}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(
            @PathVariable Long leaveId) {

        LeaveRequest rejectedLeave =
                leaveService.rejectLeave(leaveId);

        User user = userRepository.findById(rejectedLeave.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveResponse response = new LeaveResponse();
        response.setLeaveId(rejectedLeave.getId());
        response.setEmployee_id(rejectedLeave.getEmployeeId());
        response.setEmployeeName(user.getName());
        response.setStartDate(rejectedLeave.getStartDate());
        response.setEndDate(rejectedLeave.getEndDate());
        response.setStatus(rejectedLeave.getStatus());

        response.setNumberOfDays(
                ChronoUnit.DAYS.between(
                        rejectedLeave.getStartDate(),
                        rejectedLeave.getEndDate()
                ) + 1
        );

        return ResponseEntity.ok(response);
    }


}
