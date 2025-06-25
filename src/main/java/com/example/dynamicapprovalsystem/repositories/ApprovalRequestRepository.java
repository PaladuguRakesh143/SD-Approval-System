package com.example.dynamicapprovalsystem.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dynamicapprovalsystem.EnumConstants.ApprovalStatus;
import com.example.dynamicapprovalsystem.entities.ApprovalRequest;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
   
    ApprovalRequest findByWfTransactionId(String wfTransactionId);

    ApprovalRequest findByApplicationId(String applicationId);

    List<ApprovalRequest> findByCurrentApproversApproverIdAndStatus(Long approverId, ApprovalStatus status);

    List<ApprovalRequest> findByStatus(ApprovalStatus approved);
}
    


