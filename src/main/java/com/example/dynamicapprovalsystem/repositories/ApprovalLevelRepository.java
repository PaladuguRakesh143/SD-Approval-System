package com.example.dynamicapprovalsystem.repositories;

import com.example.dynamicapprovalsystem.entities.ApprovalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLevelRepository extends JpaRepository<ApprovalLevel, Long> {
   
}
