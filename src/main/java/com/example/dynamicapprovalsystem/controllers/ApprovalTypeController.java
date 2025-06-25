package com.example.dynamicapprovalsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dynamicapprovalsystem.entities.ApprovalType;
import com.example.dynamicapprovalsystem.services.ApprovalTypeService;
import java.util.*;

@RestController
@RequestMapping("/approval")
public class ApprovalTypeController {
    @Autowired
    private ApprovalTypeService approvalTypeService;

    @PostMapping
    public ResponseEntity<List<ApprovalType>> createApprovalTypes(@RequestBody List<ApprovalType> approvalTypes) {
        List<ApprovalType> createdApprovalTypes = approvalTypeService.createApprovalTypes(approvalTypes);
        return ResponseEntity.ok(createdApprovalTypes);
    }

}
