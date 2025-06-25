package com.example.dynamicapprovalsystem.DTOs;

import com.example.dynamicapprovalsystem.EnumConstants.ApprovalAction; // Make sure this is imported
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalLogDTO {
    private Long logId;
    private String applicationName;
    private ApprovalAction action; 
    private String approver;
    private Long approverId;
    private LocalDateTime submittedTime;
    private LocalDateTime approvedTime;
    private String applicationId;
    private String remarks;
    private int level;

    public ApprovalLogDTO(Long logId, String applicationName, ApprovalAction action, 
                          String approver, Long approverId, LocalDateTime submittedTime,
                          LocalDateTime approvedTime, String applicationId, String remarks, int level) {
        this.logId = logId;
        this.applicationName = applicationName;
        this.action = action; 
        this.approver = approver;
        this.approverId = approverId;
        this.submittedTime = submittedTime;
        this.approvedTime = approvedTime;
        this.applicationId = applicationId;
        this.remarks = remarks;
        this.level = level;
    }
}
