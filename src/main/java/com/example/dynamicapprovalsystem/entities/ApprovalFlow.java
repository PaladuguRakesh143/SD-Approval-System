package com.example.dynamicapprovalsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "TB_SDAP_AP_TYP_FLW")
public class ApprovalFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLOW_ID")
    private Long flowId;

    @Column(name = "FLOW_TYPE")
    private String approvalType; 

    @ManyToOne
    @JoinColumn(name = "APPROVAL_TYPE_ID")
    @JsonIgnore
    private ApprovalType approvalTypeEntity;

    @OneToMany(mappedBy = "approvalFlowEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalLevel> approvalLevels = new ArrayList<>(); 
}
