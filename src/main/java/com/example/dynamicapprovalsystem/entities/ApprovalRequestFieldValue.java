package com.example.dynamicapprovalsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_SDAP_REQ_FLD_VAL")
public class ApprovalRequestFieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "APPROVAL_REQUEST_ID", nullable = false)
    @JsonBackReference
    private ApprovalRequest approvalRequest;

    @Column(name = "FIELD_NAME", nullable = false)
    private String fieldName;

    @Column(name = "FIELD_VALUE", nullable = false)
    private String fieldValue;
}
