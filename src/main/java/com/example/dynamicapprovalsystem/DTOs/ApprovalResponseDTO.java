package com.example.dynamicapprovalsystem.DTOs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class ApprovalResponseDTO {
    private Long requestId; 
    private String applicationId;
    private String type;
    private int levels; 
    private int currentLevel;
    private String status; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ApproverDTO> currentApprovers; 
    private List<ApproverDTO> nextApprovers;
    private List<ApprovalLogDTO> logs;
    private Map<String, Object> fieldValues; 
    private String remarks; 

}

