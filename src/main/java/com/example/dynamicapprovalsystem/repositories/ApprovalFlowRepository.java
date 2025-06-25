package com.example.dynamicapprovalsystem.repositories;

import com.example.dynamicapprovalsystem.entities.ApprovalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFlowRepository extends JpaRepository<ApprovalFlow, Long> {
   
}
