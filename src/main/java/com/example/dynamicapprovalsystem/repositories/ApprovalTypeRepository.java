package com.example.dynamicapprovalsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dynamicapprovalsystem.entities.ApprovalType;

public interface ApprovalTypeRepository extends JpaRepository<ApprovalType, Long> {
    ApprovalType findByName(String name);
}
