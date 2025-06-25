package com.example.dynamicapprovalsystem.ApiResonseMessage;

import lombok.Data;
@Data
public class ApiResponse {
    private String wfTransactionId;
    private Long wfStageId;
    private String approvalStatus;
    private String message;
    private String applicationId;

    public ApiResponse() {}

    public ApiResponse(String message, String status) {
        this.message = message;
        this.approvalStatus = status;
    }

    public ApiResponse(String wfTransactionId, Long wfStageId, String approvalStatus, String message) {
        this.wfTransactionId = wfTransactionId;
        this.wfStageId = wfStageId;
        this.approvalStatus = approvalStatus;
        this.message = message;
        
    }
    public ApiResponse(String wfTransactionId, Long wfStageId, String approvalStatus, String message,String applicationId) {
        this.wfTransactionId = wfTransactionId;
        this.wfStageId = wfStageId;
        this.approvalStatus = approvalStatus;
        this.message = message;
        this.applicationId=applicationId;
    }


    public String getWfTransactionId() {
        return wfTransactionId;
    }

    public void setWfTransactionId(String wfTransactionId) {
        this.wfTransactionId = wfTransactionId;
    }

    public Long getWfStageId() {
        return wfStageId;
    }

    public void setWfStageId(Long wfStageId) {
        this.wfStageId = wfStageId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
