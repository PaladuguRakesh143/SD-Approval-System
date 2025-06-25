package com.example.dynamicapprovalsystem.DTOs;

import lombok.Data;

@Data
public class ApproverDTO {
    private Long id; 
    private Long approverId; 
    private String approverName; 

    public ApproverDTO(Long id, Long approverId, String approverName) {
        this.id = id;
        this.approverId = approverId;
        this.approverName = approverName;
    }
}