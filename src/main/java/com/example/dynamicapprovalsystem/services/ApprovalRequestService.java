package com.example.dynamicapprovalsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicapprovalsystem.entities.ApprovalRequest;
import com.example.dynamicapprovalsystem.entities.ApprovalRequestFieldValue;
import com.example.dynamicapprovalsystem.entities.ApprovalLog;
import com.example.dynamicapprovalsystem.entities.ApprovalType;
import com.example.dynamicapprovalsystem.entities.Approver;
import com.example.dynamicapprovalsystem.ApiResonseMessage.ApiResponse;
import com.example.dynamicapprovalsystem.DTOs.ApplicationCounts;
import com.example.dynamicapprovalsystem.DTOs.ApprovalDetail;
import com.example.dynamicapprovalsystem.DTOs.ApprovalDetailsResponse;
import com.example.dynamicapprovalsystem.DTOs.ApprovalLogDTO;
import com.example.dynamicapprovalsystem.DTOs.ApprovalRequestDTO;
import com.example.dynamicapprovalsystem.DTOs.ApprovalRequestFieldValueDTO;
import com.example.dynamicapprovalsystem.DTOs.ApproverDTO;
import com.example.dynamicapprovalsystem.EnumConstants.ApprovalAction;
import com.example.dynamicapprovalsystem.EnumConstants.ApprovalStatus;
import com.example.dynamicapprovalsystem.entities.ApprovalField;
import com.example.dynamicapprovalsystem.entities.ApprovalFlow;
import com.example.dynamicapprovalsystem.entities.ApprovalLevel;

import com.example.dynamicapprovalsystem.repositories.ApprovalRequestRepository;
import com.example.dynamicapprovalsystem.repositories.ApprovalTypeRepository;
import com.example.dynamicapprovalsystem.repositories.ApprovalLogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.dynamicapprovalsystem.DTOs.StatisticsResponse;

@Service
public class ApprovalRequestService {

    @Autowired
    private ApprovalRequestRepository approvalRequestRepo;

    @Autowired
    private ApprovalLogRepository approvalLogRepo;


    @Autowired
    private ApprovalTypeRepository approvalTypeRepo;

    public ApiResponse createApprovalRequest(Map<String, Object> requestData) {
        String wfTransactionId = (String) requestData.get("wfTransactionId");
        Long wfStageId = ((Number) requestData.get("wfStageId")).longValue();
        Long typeId = ((Number) requestData.get("ApprovalTypeId")).longValue();
    
        ApprovalRequest existingRequest = approvalRequestRepo.findByWfTransactionId(wfTransactionId);
        if (existingRequest != null) {
            return handleExistingRequest(existingRequest, wfTransactionId, wfStageId);
        }
    
        ApprovalType approvalType = approvalTypeRepo.findById(typeId).orElse(null);
        if (approvalType == null) {
            return new ApiResponse("Approval type not found", "Failure");
        }
    
        List<ApprovalFlow> approvalFlows = approvalType.getApprovalFlows();
        if (approvalFlows.isEmpty()) {
            return new ApiResponse("No approval flows defined for this type", "Failure");
        }
    
        int totalLevels = approvalFlows.stream()
            .mapToInt(flow -> flow.getApprovalLevels().size())
            .sum();

           

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setApplicationId("APPID" + String.format("%010d", new Random().nextInt(1_000_000_000)));
        approvalRequest.setType(approvalType.getName());
        approvalRequest.setTypeId(approvalType.getTypeId());
        approvalRequest.setLevels(totalLevels);
        approvalRequest.setCurrentLevel(1);
        approvalRequest.setCreatedAt(LocalDateTime.now());
        approvalRequest.setWfTransactionId(wfTransactionId);
        approvalRequest.setWfStageId(wfStageId);
        approvalRequest.setStatus(ApprovalStatus.PENDING);
    
        for (ApprovalFlow flow : approvalFlows) {
            List<ApprovalLevel> levels = flow.getApprovalLevels();
            if (!levels.isEmpty()) {
                ApprovalLevel currentApprovalLevel = levels.get(0);
                approvalRequest.getCurrentApprovers().addAll(currentApprovalLevel.getApprovers());
    
                if (levels.size() > 1) {
                    ApprovalLevel nextApprovalLevel = levels.get(1);
                    approvalRequest.getNextApprovers().addAll(nextApprovalLevel.getApprovers());
                }
            }
        }
    
        ApprovalRequest ap=approvalRequestRepo.save(approvalRequest); 
        Map<String, String> userData = (Map<String, String>) requestData.get("userData");
        List<String> validFieldNames = getValidFieldNamesForApprovalType(approvalType); 
    
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
    
            if (validFieldNames.contains(fieldName)) {
                ApprovalRequestFieldValue fieldValueEntity = new ApprovalRequestFieldValue();
                fieldValueEntity.setFieldName(fieldName);
                fieldValueEntity.setFieldValue(fieldValue);
                fieldValueEntity.setApprovalRequest(ap); 
                ap.getFieldValues().add(fieldValueEntity); 
            }
            
        }    
    
        
        ApprovalLog log = new ApprovalLog();
        log.setAction(ApprovalAction.CREATED);
        log.setSubmittedTime(LocalDateTime.now());
        log.setApplicationId(approvalRequest.getApplicationId());
        log.setApprovalRequest(approvalRequest);
        log.setApplicationName(approvalType.getName());
        log.setApprover("-");
        log.setLevel(0);
        log.setRemarks("-");
        log.setMessage(approvalType.getName()+" application is submitted");
        approvalLogRepo.save(log); 
        ap.addLog(log);
        approvalRequestRepo.save(ap);
        return new ApiResponse(wfTransactionId, wfStageId, "PND", "Pending in the approval level 1",ap.getApplicationId());
    }
    private List<String> getValidFieldNamesForApprovalType(ApprovalType approvalType) {
       return approvalType.getFields().stream()
                .map(ApprovalField::getFieldName)
                .collect(Collectors.toList());
    }
    
    private ApiResponse handleExistingRequest(ApprovalRequest existingRequest, String wfTransactionId, Long wfStageId) {
        String currentStatus = existingRequest.getStatus().name();
        String applicationId=existingRequest.getApplicationId();
        String statusMessage = determineCurrentStatusMessage(currentStatus, existingRequest.getCurrentLevel());
        if(currentStatus=="PENDING"){
            currentStatus="PND";
        }else if(currentStatus=="APPROVED"){
            currentStatus="COM";
        }else if(currentStatus=="RECALLED"){
            currentStatus="RCL";
        }else if(currentStatus=="CANCELLED"){
            currentStatus="CAN";
        }else{
            currentStatus="REJ";
        }
        return new ApiResponse(wfTransactionId, wfStageId, currentStatus, statusMessage,applicationId);
    }
    
    
    private String determineCurrentStatusMessage(String currentStatus, int currentStage) {
        switch (currentStatus) {
            case "PENDING": return "Pending in approval level " + currentStage;
            case "APPROVED": return "Completed in approval level " + currentStage;
            case "RECALLED": return "Recalled in approval level " + currentStage;
            case "CANCELLED": return "Cancelled in approval level " + currentStage;
            default: return "Rejected in approval " + currentStage;
        }
    }
    
    

    public ApiResponse processApproval(Map<String, Object> approvalData) {
        String applicationId = (String) approvalData.get("applicationId");
        Long approverId = ((Number) approvalData.get("approverId")).longValue();
        String status = (String) approvalData.get("status");
        String comment = (String) approvalData.get("comment");
    
        ApprovalRequest request = approvalRequestRepo.findByApplicationId(applicationId);
        if (request == null) {
            return new ApiResponse("Request not found", "Failure");
        }

        if (!isRequestProcessable(request)) {
            return new ApiResponse("Request cannot be processed: " + request.getStatus(), "Failure");
        }
    
        Approver currentApprover = getCurrentApprover(request, approverId);
        if (currentApprover == null) {
            return new ApiResponse("Approver not eligible for this request.", "Failure");
        }
    
        ApprovalLog log = createApprovalLog(request, currentApprover, status, comment);
    
        switch (status.toLowerCase()) {
            case "approve":
                return processApprovalAction(request, log, currentApprover, ApprovalAction.APPROVED);
            case "reject":
                return processApprovalAction(request, log, currentApprover, ApprovalAction.REJECTED);
            case "recall":
                return processRecallAction(request, log, currentApprover);
            default:
                return new ApiResponse("Invalid status provided", "Failure");
        }
    }
    
    private boolean isRequestProcessable(ApprovalRequest request) {
        return request.getStatus() == ApprovalStatus.PENDING;
    }
    
    private Approver getCurrentApprover(ApprovalRequest request, Long approverId) {
        return request.getCurrentApprovers().stream()
                .filter(approver -> approver.getApproverId().equals(approverId)) 
                .findFirst().orElse(null);
    }
    
    private ApprovalLog createApprovalLog(ApprovalRequest request, Approver approver, String status, String comment) {
        ApprovalLog log = new ApprovalLog();
        log.setAction(status.equalsIgnoreCase("approve") ? ApprovalAction.APPROVED :
                       status.equalsIgnoreCase("reject") ? ApprovalAction.REJECTED :
                       ApprovalAction.RECALLED);
        log.setApprovedTime(LocalDateTime.now());
        log.setApplicationId(request.getApplicationId());
        log.setApprover(approver.getApproverName());
        log.setRemarks(comment);
        log.setApprovalRequest(request); 
        log.setLevel(request.getCurrentLevel());
        log.setApproverId(approver.getId());
        log.setApplicationName(request.getType());
        log.setSubmittedTime(request.getCreatedAt());
        log.setApprovedTime(request.getCreatedAt());

    StringBuilder message = new StringBuilder();
    message.append(request.getType()).append(" application is ");
    if (status.equalsIgnoreCase("approve")) {
        message.append("approved by ").append(approver.getApproverName());
        
        List<Approver> nextApprovers = request.getNextApprovers();
        if (!nextApprovers.isEmpty()) {
            message.append(", forwarded to ").append(nextApprovers.get(0).getApproverName());
        }else {
            message.append(", no further approvers required.");
        }
    } else if (status.equalsIgnoreCase("reject")) {
        message.append("rejected by ").append(approver.getApproverName());
    } else if (status.equalsIgnoreCase("recall")) {
        message.append("recalled by ").append(approver.getApproverName());
    }
    
    log.setMessage(message.toString());
    return log;
    }
    
    private ApiResponse processApprovalAction(ApprovalRequest request, ApprovalLog log, Approver approver, ApprovalAction action) {
        request.setUpdatedAt(LocalDateTime.now());
        approvalLogRepo.save(log);
        
        if (action == ApprovalAction.APPROVED) {
            if (request.getCurrentLevel() < request.getLevels()) {
                request.setStatus(ApprovalStatus.PENDING);
                moveToNextApprover(request); 
            } else {
                request.setStatus(ApprovalStatus.APPROVED);
                request.setCurrentApprovers(Collections.emptyList());
                request.setNextApprovers(Collections.emptyList());
            }
        } else if (action == ApprovalAction.REJECTED) {
            request.setStatus(ApprovalStatus.REJECTED);
            request.setCurrentApprovers(Collections.emptyList());
            request.setNextApprovers(Collections.emptyList());
        } else if (action == ApprovalAction.RECALLED) {
            return processRecallAction(request, log, approver);
        }
        
        approvalRequestRepo.save(request);
        
        return new ApiResponse(request.getWfTransactionId(), request.getWfStageId(), "Success", 
                               "Request " + action.name() + " successfully by " + approver.getApproverName(),request.getApplicationId());
    }
    
    
    private ApiResponse processRecallAction(ApprovalRequest request, ApprovalLog log, Approver approver) {
        request.setStatus(ApprovalStatus.RECALLED);
        request.setCurrentLevel(1); 
        request.setCurrentApprovers(findApproversForNextLevel(request, 1)); 
        List<Approver> nextApprovers = findApproversForNextLevel(request, 2); 
        request.setNextApprovers(nextApprovers); 
        request.setUpdatedAt(LocalDateTime.now());
    
        approvalLogRepo.save(log);
        approvalRequestRepo.save(request);
        
        return new ApiResponse(request.getWfTransactionId(), request.getWfStageId(), "Success", 
                               "Request recalled successfully by " + approver.getApproverName(),request.getApplicationId());
    }
    
    private void moveToNextApprover(ApprovalRequest request) {
        int nextLevel = request.getCurrentLevel() + 1;
        if (nextLevel > request.getLevels()) {
            request.setCurrentApprovers(Collections.emptyList());
            request.setNextApprovers(Collections.emptyList());
            return;
        }
        List<Approver> nextApprovers = findApproversForNextLevel(request, nextLevel);
        
        if (!nextApprovers.isEmpty()) {
            request.setCurrentLevel(nextLevel);
            request.setCurrentApprovers(nextApprovers);
            request.setNextApprovers(Collections.emptyList());
        } else {
            request.setCurrentApprovers(Collections.emptyList());
            request.setNextApprovers(Collections.emptyList());
        }
    }
    
    private List<Approver> findApproversForNextLevel(ApprovalRequest request, int nextLevel) {
        ApprovalType approvalType = approvalTypeRepo.findById(request.getTypeId()).orElse(null);
        if (approvalType == null) {
            return new ArrayList<>(); 
        }
    
        List<ApprovalFlow> approvalFlows = approvalType.getApprovalFlows();
        if (approvalFlows.isEmpty()) {
            return new ArrayList<>(); 
        }
    
        ApprovalFlow currentFlow = approvalFlows.get(0); 
        List<ApprovalLevel> levels = currentFlow.getApprovalLevels();
        
        if (nextLevel - 1 < levels.size()) {
            ApprovalLevel nextApprovalLevel = levels.get(nextLevel - 1); 
            return new ArrayList<>(nextApprovalLevel.getApprovers()); 
        }
    
        return new ArrayList<>(); 
    }
    
    public ApiResponse editAndResubmitApprovalRequest(Map<String, Object> requestData) {
        String applicationId = (String) requestData.get("applicationId");
        ApprovalRequest request = approvalRequestRepo.findByApplicationId(applicationId);
        
        if (request == null) {
            return new ApiResponse("Request not found", "Failure");
        }
    
        if (request.getStatus() != ApprovalStatus.RECALLED) {
            return new ApiResponse("Cannot edit! Request is not recalled.", "Failure");
        }
    
        request.setStatus(ApprovalStatus.PENDING);
        request.setUpdatedAt(LocalDateTime.now());
    
        request.getFieldValues().clear();
        Map<String, Object> userData = (Map<String, Object>) requestData.get("userData");
        if (userData != null) {
            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                ApprovalRequestFieldValue fieldValue = new ApprovalRequestFieldValue();
                fieldValue.setApprovalRequest(request);
                fieldValue.setFieldName(entry.getKey());
                fieldValue.setFieldValue(entry.getValue().toString());
                request.getFieldValues().add(fieldValue);
            }
        }
    
        ApprovalLog log = new ApprovalLog();
        log.setAction(ApprovalAction.UPDATED);
        log.setSubmittedTime(LocalDateTime.now());
        log.setApplicationId(request.getApplicationId());
        log.setApprovalRequest(request); 
        log.setApplicationName(request.getType());
        log.setApprover("-");
        log.setLevel(0);
        log.setRemarks("-");
        log.setMessage(request.getType()+" application is updated");
        log.setApprovedTime(request.getCreatedAt());
        approvalLogRepo.save(log);
        request.addLog(log); 
        approvalRequestRepo.save(request);
        return new ApiResponse("Approval request updated and resubmitted successfully", "Success");
    }
    

    public ApprovalRequest getApprovalRequestById(Long id) {
        return approvalRequestRepo.findById(id).orElse(null);
    }

    public List<ApprovalLog> getLogsByApplicationId(String applicationId) {
        return approvalLogRepo.findByApplicationId(applicationId);
    }

    public StatisticsResponse getApplicationStatistics() {
        List<ApprovalRequest> requests = approvalRequestRepo.findAll();
    
        int totalApplications = requests.size();
        int completedApplications = (int) requests.stream()
                .filter(request -> request.getStatus().equals(ApprovalStatus.APPROVED))
                .count();
        int pendingApplications = (int) requests.stream()
                .filter(request -> request.getStatus().equals(ApprovalStatus.PENDING))
                .count();
        int rejectedApplications = (int) requests.stream()
                .filter(request -> request.getStatus().equals(ApprovalStatus.REJECTED))
                .count();
    
        ApplicationCounts counts = new ApplicationCounts(totalApplications, completedApplications, pendingApplications, rejectedApplications);
        
        return new StatisticsResponse(Collections.singletonList(counts));
    }
    

    public List<ApprovalRequest> getPendingRequests(Long approverId) {
        return approvalRequestRepo.findByCurrentApproversApproverIdAndStatus(approverId, ApprovalStatus.PENDING);
    }

    public List<ApprovalRequest> getApprovedRequests() {
        return approvalRequestRepo.findByStatus(ApprovalStatus.APPROVED);
    }

    public List<ApprovalRequest> getRejectedRequests() {
        return approvalRequestRepo.findByStatus(ApprovalStatus.REJECTED);
    }

    public List<ApprovalRequest> getAllApplications() {
        return approvalRequestRepo.findAll();
    }

public List<ApprovalRequestDTO> getRequestsByStatus(Long approverId, String status) {
    List<ApprovalRequest> requests;

    switch (status.toLowerCase()) {
        case "pending":
            requests = getPendingRequests(approverId);
            break;
        case "approved":
            requests = getApprovedRequests();
            break;
        case "rejected":
            requests = getRejectedRequests();
            break;
        case "all":
            requests = getAllApplications();
            break;    
        default:
            throw new IllegalArgumentException("Invalid status: " + status);
    }

    return requests.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
}

private ApprovalRequestDTO convertToDTO(ApprovalRequest request) {
    ApprovalRequestDTO dto = new ApprovalRequestDTO();
    
    dto.setRequestId(request.getRequestId());
    dto.setApplicationId(request.getApplicationId());
    dto.setType(request.getType());
    dto.setTypeId(request.getTypeId());
    dto.setLevels(request.getLevels());
    dto.setCurrentLevel(request.getCurrentLevel());
    dto.setCreatedAt(request.getCreatedAt().toString()); 
    dto.setUpdatedAt(request.getUpdatedAt() != null ? request.getUpdatedAt().toString() : null);
    dto.setStatus(request.getStatus());
    dto.setCancelled(request.isCancelled());
    dto.setWfTransactionId(request.getWfTransactionId());
    dto.setWfStageId(request.getWfStageId());

    dto.setCurrentApprovers(request.getCurrentApprovers().stream()
        .map((Approver approver) -> 
            new ApproverDTO(approver.getId(), approver.getApproverId(), approver.getApproverName()))
        .collect(Collectors.toList()));

    dto.setNextApprovers(request.getNextApprovers().stream()
        .map((Approver approver) -> 
            new ApproverDTO(approver.getId(), approver.getApproverId(), approver.getApproverName()))
        .collect(Collectors.toList()));

    dto.setFieldValues(request.getFieldValues().stream()
        .map((ApprovalRequestFieldValue fieldValue) -> 
            new ApprovalRequestFieldValueDTO(fieldValue.getId(), fieldValue.getFieldName(), fieldValue.getFieldValue()))
        .collect(Collectors.toList()));

    dto.setLogs(request.getLogs().stream()
        .map((ApprovalLog log) -> 
            new ApprovalLogDTO(log.getLog_id(), log.getApplicationName(), log.getAction(),
                log.getApprover(), log.getApproverId(), log.getSubmittedTime(),
                log.getApprovedTime(), log.getApplicationId(), log.getRemarks(), log.getLevel()))
        .collect(Collectors.toList()));

    return dto;
}

    public ApiResponse cancelApprovalRequest(String applicationId) {
        ApprovalRequest approvalRequest = approvalRequestRepo.findByApplicationId(applicationId);
        
        if (approvalRequest == null) {
            return new ApiResponse("Approval request not found", "Failure");
        }
    
        if (approvalRequest.getStatus() == ApprovalStatus.PENDING && !approvalRequest.isCancelled()) {
            approvalRequest.setCancelled(true);
            approvalRequest.setStatus(ApprovalStatus.CANCELLED);
            approvalRequest.setCurrentLevel(0);
            approvalRequest.setCurrentApprovers(null);
            approvalRequest.setNextApprovers(null);
            approvalRequestRepo.save(approvalRequest);
            return new ApiResponse("Approval request cancelled successfully", "Success");
        } else {
            return new ApiResponse("Request cannot be cancelled. It's already processed or cancelled.", "Failure");
        }
    }


    public ApprovalDetailsResponse getApprovalDetailsByApplicationId(String applicationId) {
        ApprovalRequest request = approvalRequestRepo.findByApplicationId(applicationId);
        if (request == null) {
            return null; 
        }
    
        ApprovalDetailsResponse response = new ApprovalDetailsResponse();
    
        List<ApprovalLog> approvedLogs = request.getLogs().stream()
            .filter(log -> log.getAction() == ApprovalAction.APPROVED)
            .collect(Collectors.toList());
    
        for (ApprovalLog log : approvedLogs) {
            response.getApprovedDetails().add(new ApprovalDetail(
                log.getApprover(),
                log.getApproverId(),
                log.getSubmittedTime(),
                log.getApprovedTime(),
                request.getCurrentLevel()
            ));
        }
    
        List<Approver> currentApprovers = request.getCurrentApprovers();
        if (!currentApprovers.isEmpty()) {
            for (Approver approver : currentApprovers) {
                response.getPendingApprovalDetails().add(new ApprovalDetail(
                    approver.getApproverName(),
                    approver.getApproverId(),
                    request.getCreatedAt(),
                    null, 
                    request.getCurrentLevel()
                ));
            }
        }
    
        List<Approver> nextApprovers = request.getNextApprovers();
        for (Approver approver : nextApprovers) {
            response.getYetToReceiveDetails().add(new ApprovalDetail(
                approver.getApproverName(),
                approver.getApproverId(),
                request.getCreatedAt(),
                null, 
                request.getCurrentLevel() + 1 
            ));
        }
    
        return response; 
    }
    
    
}
