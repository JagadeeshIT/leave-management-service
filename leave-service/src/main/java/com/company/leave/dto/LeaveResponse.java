package com.company.leave.dto;

import java.time.LocalDate;

import com.company.leave.leave.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "leaveId",
        "employee_id",
        "employeeName",
        "startDate",
        "endDate",
        "numberOfDays",
        "status"
})
public class LeaveResponse {

    private Long leaveId;
    private String employeeName;
    private Long employee_id;
    private LocalDate startDate;
    private LocalDate endDate;
    private long numberOfDays;
    private LeaveStatus status;
}
