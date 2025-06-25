package com.example.dynamicapprovalsystem.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dynamicapprovalsystem.entities.ApprovalFlow;
import com.example.dynamicapprovalsystem.entities.ApprovalLevel;
import com.example.dynamicapprovalsystem.entities.ApprovalType;
import com.example.dynamicapprovalsystem.entities.Approver;
import com.example.dynamicapprovalsystem.repositories.ApprovalTypeRepository;

@Service
public class ApprovalTypeService {
    @Autowired
    private ApprovalTypeRepository approvalTypeRepo;

    public List<ApprovalType> createApprovalTypes(List<ApprovalType> approvalTypes) {
        for (ApprovalType approvalType : approvalTypes) {
            approvalType.setCreatedAt( LocalDateTime.now().withSecond(0).withNano(0));
            for (ApprovalFlow flow : approvalType.getApprovalFlows()) {
                flow.setApprovalTypeEntity(approvalType);  
                for (ApprovalLevel level : flow.getApprovalLevels()) {
                    level.setApprovalFlowEntity(flow); 
                    for (Approver approver : level.getApprovers()) {
                        approver.setApprovalLevelEntity(level); 
                    }
                }
            }
        }
       
        return approvalTypeRepo.saveAll(approvalTypes);
    }
}
