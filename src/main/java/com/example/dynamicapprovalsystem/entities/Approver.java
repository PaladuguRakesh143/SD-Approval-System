package com.example.dynamicapprovalsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_SDAP_AP_TYP_FLW_LVL_APR")
public class Approver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "APPROVER_ID")
    private Long approverId;

    @Column(name = "APPROVER_NAME")
    private String approverName;

    @ManyToOne
    @JoinColumn(name = "APPROVAL_LEVEL_ID")
    @JsonIgnore
    private ApprovalLevel approvalLevelEntity;
}
