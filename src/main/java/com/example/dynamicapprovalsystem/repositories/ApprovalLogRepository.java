package com.example.dynamicapprovalsystem.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dynamicapprovalsystem.entities.ApprovalLog;

public interface ApprovalLogRepository extends JpaRepository<ApprovalLog, Long> {

    List<ApprovalLog> findByApplicationId(String applicationId);
}