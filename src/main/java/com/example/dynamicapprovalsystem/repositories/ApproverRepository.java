package com.example.dynamicapprovalsystem.repositories;

import com.example.dynamicapprovalsystem.entities.Approver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproverRepository extends JpaRepository<Approver, Long> {
   
   
}
