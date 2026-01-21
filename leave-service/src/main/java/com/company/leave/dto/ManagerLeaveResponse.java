package com.company.leave.dto;

import java.time.LocalDate;

import com.company.leave.leave.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({
    "leaveId",
    "employeeId",
    "employeeName",
    "startDate",
    "endDate",
    "status"
})
public class ManagerLeaveResponse {

    private Long leaveId;
    private Long employeeId;
    private String employeeName;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private long numberOfDays;
    private LeaveStatus status;
}


// AS OF NOW IT IS NOT USING (FOR FUTURE)