package com.example.dynamicapprovalsystem.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_SDAP_TYP_FLD")
public class ApprovalField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FIELD_ID")
    private Long id;

    @Column(name = "FIELD_NAME", nullable = false)
    private String fieldName;

    @Column(name = "FIELD_TYPE", nullable = false)
    private String fieldType;

    @Column(name = "REQUIRED", nullable = false)
    private boolean required;
}
