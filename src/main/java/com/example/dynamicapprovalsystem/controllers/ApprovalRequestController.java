package com.example.dynamicapprovalsystem.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamicapprovalsystem.ApiResonseMessage.ApiResponse;
import com.example.dynamicapprovalsystem.DTOs.ApprovalDetailsResponse;
import com.example.dynamicapprovalsystem.DTOs.ApprovalRequestDTO;
import com.example.dynamicapprovalsystem.DTOs.StatisticsResponse;
import com.example.dynamicapprovalsystem.entities.ApprovalLog;
import com.example.dynamicapprovalsystem.entities.ApprovalRequest;
import com.example.dynamicapprovalsystem.repositories.ApprovalRequestRepository;
import com.example.dynamicapprovalsystem.services.ApprovalRequestService;

@RestController
@RequestMapping("/requests")
public class ApprovalRequestController {

    @Autowired
    private ApprovalRequestService approvalRequestService;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    @PostMapping("/create")
    public ApiResponse createApprovalRequest(@RequestBody Map<String, Object> requestData) {
        return approvalRequestService.createApprovalRequest(requestData);
    }

    @PostMapping("/process")
    public ApiResponse processApproval(@RequestBody Map<String, Object> approvalData) {
        return approvalRequestService.processApproval(approvalData);
    }

    @PostMapping("/edit")
    public ApiResponse editAndResubmitApprovalRequest(@RequestBody Map<String, Object> requestData) {
        return approvalRequestService.editAndResubmitApprovalRequest(requestData);
    }

    @PostMapping("/get/{id}")
    public ApprovalRequest getApprovalRequestById(@PathVariable Long id) {
        return approvalRequestService.getApprovalRequestById(id);
    }

    @PostMapping("/getall")
    public List<ApprovalRequest> getAllApprovalRequests() {
        return approvalRequestRepository.findAll();
    }

    @PostMapping("/statistics")
    public StatisticsResponse  getApplicationStatistics() {
        return approvalRequestService.getApplicationStatistics();
    }

   @PostMapping("/pending/{approverId}")
    public List<ApprovalRequest> getPendingRequests(@PathVariable Long approverId) {
        return approvalRequestService.getPendingRequests(approverId);
    }

    @PostMapping("/approved")
    public List<ApprovalRequest> getApprovedRequests() {
        return approvalRequestService.getApprovedRequests();
    }

    @PostMapping("/rejected")
    public List<ApprovalRequest> getRejectedRequests() {
        return approvalRequestService.getRejectedRequests();
    }

    @PostMapping("/all")
    public List<ApprovalRequest> getAllApplications() {
        return approvalRequestService.getAllApplications();
    }

    @PostMapping("/cancel-request/{applicationID}")
    public ApiResponse cancelApprovalRequest(@PathVariable String applicationID) {
        return approvalRequestService.cancelApprovalRequest(applicationID);
    }

    @PostMapping("/logs/{applicationId}")
    public List<ApprovalLog> getLogByApplicationId(@PathVariable String applicationId) {
        return approvalRequestService.getLogsByApplicationId(applicationId);
    }

    @PostMapping("/summary/{approverId}/{status}")
    public List<ApprovalRequestDTO> getApprovalRequests(
            @PathVariable Long approverId,
            @PathVariable String status) {
        return approvalRequestService.getRequestsByStatus(approverId, status);
    }

    @PostMapping("/details/{applicationId}")
    public ApprovalDetailsResponse getAppprovalDetails(@PathVariable String applicationId) {
        return approvalRequestService.getApprovalDetailsByApplicationId(applicationId);
    }
    
    
}
