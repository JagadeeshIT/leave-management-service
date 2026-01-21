package com.company.leave.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.leave.dto.DepartmentLeaveStatsResponse;
import com.company.leave.service.LeaveService;

@RestController
@RequestMapping("/admin/leaves")
public class AdminLeaveController {

    private final LeaveService leaveService;

    public AdminLeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<DepartmentLeaveStatsResponse>> getLeaveStatistics() {
        return ResponseEntity.ok(
                leaveService.getLeaveStatisticsByDepartment()
        );
    }
}

