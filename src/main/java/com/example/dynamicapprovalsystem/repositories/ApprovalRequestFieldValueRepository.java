package com.example.dynamicapprovalsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.dynamicapprovalsystem.entities.ApprovalRequest;
import com.example.dynamicapprovalsystem.entities.ApprovalRequestFieldValue;

public interface ApprovalRequestFieldValueRepository extends JpaRepository<ApprovalRequestFieldValue, Long>{

    List<ApprovalRequestFieldValue> findByApprovalRequest(ApprovalRequest approvalRequest);
}
