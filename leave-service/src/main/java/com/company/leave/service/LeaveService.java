package com.company.leave.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.company.leave.dto.ApplyLeaveRequest;
import com.company.leave.dto.DepartmentLeaveStatsResponse;
import com.company.leave.exception.BusinessException;
import com.company.leave.exception.ResourceNotFoundException;
import com.company.leave.leave.LeaveRequest;
import com.company.leave.leave.LeaveStatus;
import com.company.leave.leave.User;
import com.company.leave.repository.LeaveRequestRepository;
import com.company.leave.repository.UserRepository;

@Service
public class LeaveService {
	
	private final LeaveRequestRepository leaveRequestRepository;
	private final UserRepository userRepository;
	
	public LeaveService(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
		this.userRepository = userRepository;
	}
	
	
	//Employee Methods
	
	public LeaveRequest applyLeave(Long employeeId, ApplyLeaveRequest request) {
		
		User user = userRepository.findById(employeeId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User not found with id: " + employeeId
	                    ));

	    // 2. Business validation – date range
	    if (request.getStartDate().isAfter(request.getEndDate())) {
	        throw new BusinessException(
	                "Start date cannot be after end date"
	        );
	    }

	    LeaveRequest leave = new LeaveRequest();

	    leave.setEmployeeId(employeeId);
	    //leave.setEmployeeName(request.getEmployeeName()); 

	    leave.setStartDate(request.getStartDate());
	    leave.setEndDate(request.getEndDate());
	    leave.setReason(request.getReason());

	    leave.setStatus(LeaveStatus.PENDING);
	    leave.setCreatedAt(LocalDateTime.now());

	    return leaveRequestRepository.save(leave);
	}

	
	public List<LeaveRequest> getLeavesByEmployee(Long employeeId) {
	    return leaveRequestRepository.findByEmployeeId(employeeId);
	}
	
	

	public LeaveRequest cancelLeave(Long employeeId, Long leaveId) {

	    LeaveRequest leave = leaveRequestRepository.findById(leaveId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Leave not found with id: " + leaveId));

	    // Ownership validation
	    if (!leave.getEmployeeId().equals(employeeId)) {
	        throw new BusinessException("You are not allowed to cancel this leave");
	    }

	    // Status validation
	    if (leave.getStatus() != LeaveStatus.PENDING) {
	        throw new BusinessException(
	                "Only pending leaves can be cancelled. Current status: " + leave.getStatus());
	    }

	    leave.setStatus(LeaveStatus.CANCELLED);
	    leave.setCreatedAt(LocalDateTime.now());

	    return leaveRequestRepository.save(leave);
	}

	
	
	//Manager Methods
	
	public List<LeaveRequest> getPendingLeaves() {
	    return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
	}

	public LeaveRequest approveLeave(Long leaveId) {

	    LeaveRequest leave = leaveRequestRepository.findById(leaveId)
	            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

	    if (!leave.getStatus().equals(LeaveStatus.PENDING)) {
	        throw new BusinessException("Only pending leaves can be approved");
	    }

	    leave.setStatus(LeaveStatus.APPROVED);
	    return leaveRequestRepository.save(leave);
	}

	public LeaveRequest rejectLeave(Long leaveId) {

	    LeaveRequest leave = leaveRequestRepository.findById(leaveId)
	            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

	    if (!leave.getStatus().equals(LeaveStatus.PENDING)) {
	        throw new BusinessException("Only pending leaves can be rejected");
	    }

	    leave.setStatus(LeaveStatus.REJECTED);
	    return leaveRequestRepository.save(leave);
	}

	
	//Admin Leave
	
	public List<DepartmentLeaveStatsResponse> getLeaveStatisticsByDepartment() {

	    // 1. Fetch all leave requests
	    List<LeaveRequest> leaves = leaveRequestRepository.findAll();

	    // 2. Fetch all users
	    List<User> users = userRepository.findAll();

	    // 3. Build employeeId -> department map
	    Map<Long, String> employeeDepartmentMap =
	            users.stream()
	                 .filter(u -> u.getDepartment() != null)
	                 .collect(Collectors.toMap(
	                     User::getId,
	                     User::getDepartment
	                 ));

	    // 4. Group leave requests by department using employeeId
	    Map<String, List<LeaveRequest>> leavesByDepartment =
	            leaves.stream()
	                  .filter(l -> employeeDepartmentMap.containsKey(l.getEmployeeId()))
	                  .collect(Collectors.groupingBy(
	                      l -> employeeDepartmentMap.get(l.getEmployeeId())
	                  ));

	    // 5. Build response
	    List<DepartmentLeaveStatsResponse> response = new ArrayList<>();

	    for (Map.Entry<String, List<LeaveRequest>> entry : leavesByDepartment.entrySet()) {

	        String department = entry.getKey();
	        List<LeaveRequest> deptLeaves = entry.getValue();

	        // Count leave statuses in one pass
	        Map<LeaveStatus, Long> statusCount =
	                deptLeaves.stream()
	                          .collect(Collectors.groupingBy(
	                              LeaveRequest::getStatus,
	                              Collectors.counting()
	                          ));

	        DepartmentLeaveStatsResponse stats = new DepartmentLeaveStatsResponse();
	        stats.setDepartment(department);
	        stats.setPending(statusCount.getOrDefault(LeaveStatus.PENDING, 0L));
	        stats.setApproved(statusCount.getOrDefault(LeaveStatus.APPROVED, 0L));
	        stats.setRejected(statusCount.getOrDefault(LeaveStatus.REJECTED, 0L));

	        response.add(stats);
	    }

	    return response;
	}




}
