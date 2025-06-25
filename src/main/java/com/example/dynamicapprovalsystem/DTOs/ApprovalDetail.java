package com.example.dynamicapprovalsystem.DTOs;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApprovalDetail {
    private String approverName;
    private Long approverId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime submittedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime approvedTime;
    private int level;

    public ApprovalDetail(String approverName, Long approverId, LocalDateTime submittedOn, LocalDateTime approvedOn, int level) {
        this.approverName = approverName;
        this.approverId = approverId;
        this.submittedTime = submittedOn;
        this.approvedTime = approvedOn;
        this.level = level;
    }
}
