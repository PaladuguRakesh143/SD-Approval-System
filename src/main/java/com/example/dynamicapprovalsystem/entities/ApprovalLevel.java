package com.example.dynamicapprovalsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "TB_SDAP_AP_TYP_FLW_LVL")
public class ApprovalLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEVEL_ID")
    private Long levelId;
    @Column(name = "APPROVAL_LEVEL")
    private String level;
    @Column(name = "LEVEL_NAME")
    private String levelName;

    @Column(name="REQUIRED")
    private int required;

    @ManyToOne
    @JoinColumn(name = "APPROVAL_FLOW_ID")
    private ApprovalFlow approvalFlowEntity;

    @OneToMany(mappedBy = "approvalLevelEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approver> approvers = new ArrayList<>(); // Initialize the list
}
