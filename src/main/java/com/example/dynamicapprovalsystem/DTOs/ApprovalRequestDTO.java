package com.example.dynamicapprovalsystem.DTOs;

import lombok.Data;
import java.util.List;

import com.example.dynamicapprovalsystem.EnumConstants.ApprovalStatus;

@Data
public class ApprovalRequestDTO {
    private Long requestId;
    private String applicationId;
    private String type;
    private Long typeId;
    private int levels;
    private int currentLevel;
    private String createdAt;
    private String updatedAt;
    private ApprovalStatus status;
    private boolean cancelled;
    private String wfTransactionId;
    private Long wfStageId;
    private List<ApproverDTO> currentApprovers;
    private List<ApproverDTO> nextApprovers;
    private List<ApprovalRequestFieldValueDTO> fieldValues;
    private List<ApprovalLogDTO> logs;

    
}