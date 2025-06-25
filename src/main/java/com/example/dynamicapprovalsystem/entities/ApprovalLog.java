package com.example.dynamicapprovalsystem.entities;

import java.time.LocalDateTime;

import com.example.dynamicapprovalsystem.EnumConstants.ApprovalAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "`TB_SDAP_REQ_LOG`") 
public class ApprovalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "LOG_ID")
    private Long log_id;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;


    @Column(name = "APPROVAL_ACTION")
    private ApprovalAction action; 

    @Column(name = "APPROVER")
    private String approver; 

    @Column(name = "APPROVER_ID")
    private Long approverId; 
    
    @Column(name = "SUBMITTED_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime submittedTime;

    @Column(name = "APPROVED_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime approvedTime;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "APPROVAL_LEVEL")
    private int level;

    @Column(name="MESSAGE")
    private String message;


    @ManyToOne
    @JoinColumn(name = "REQUEST_ID", nullable = false) 
    @JsonIgnore
    private ApprovalRequest approvalRequest;
   
}
