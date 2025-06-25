package com.example.dynamicapprovalsystem.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.dynamicapprovalsystem.EnumConstants.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "`TB_SDAP_REQ_MASTER`") 
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Long requestId;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "APPROVAL_TYPE")
    private String type; 

    @Column(name = "APPROVAL_TYPE_ID")
    private Long typeId; 

    @Column(name = "APPROVAL_LEVELS")
    private int levels; 

    @Column(name = "CURRENT_APPROVAL_LEVEL")
    private int currentLevel; 

    @Column(name = "CREATED_TS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_TS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    @Column(name = "APPROVAL_STATUS")
    private ApprovalStatus status = ApprovalStatus.PENDING; 

    @Column(name = "APPROVAL_SOFT_DELETE")
    private boolean cancelled = false;

    @Column(name = "WORKFLOW_TRANSACTION_ID")
    private String wfTransactionId;

    @Column(name = "WORKFLOW_STAGE_ID")
    private Long wfStageId;
    
    @ManyToMany
    @JoinTable(
        name = "TB_SDAP_CURRENT_APPROVERS", 
        joinColumns = @JoinColumn(name = "REQUEST_ID"), 
        inverseJoinColumns = @JoinColumn(name = "APPROVER_ID")
    )
    private List<Approver> currentApprovers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "TB_SDAP_NEXT_APPROVERS",
        joinColumns = @JoinColumn(name = "REQUEST_ID"), 
        inverseJoinColumns = @JoinColumn(name = "APPROVER_ID") 
    )
    private List<Approver> nextApprovers = new ArrayList<>();

    @OneToMany(mappedBy = "approvalRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ApprovalRequestFieldValue> fieldValues = new ArrayList<>();
    
    @OneToMany(mappedBy = "approvalRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalLog> logs = new ArrayList<>();

    public void addLog(ApprovalLog log) {
        logs.add(log);
    }
}
