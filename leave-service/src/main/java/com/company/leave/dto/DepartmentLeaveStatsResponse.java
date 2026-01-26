package com.company.leave.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "department",
        "pending",
        "approved",
        "rejected"
})
public class DepartmentLeaveStatsResponse {

    private String department;
    private long pending;
    private long approved;
    private long rejected;
}
