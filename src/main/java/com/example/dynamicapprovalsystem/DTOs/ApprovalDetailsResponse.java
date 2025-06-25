package com.example.dynamicapprovalsystem.DTOs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ApprovalDetailsResponse {
    private List<ApprovalDetail> approvedDetails = new ArrayList<>();
    private List<ApprovalDetail> pendingApprovalDetails = new ArrayList<>();
    private List<ApprovalDetail> yetToReceiveDetails = new ArrayList<>();
}