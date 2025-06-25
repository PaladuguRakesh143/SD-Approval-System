package com.example.dynamicapprovalsystem.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_SDAP_AP_TYP")
public class ApprovalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TYPE_ID")
    private Long typeId;

    @Column(name = "TYPE_NAME", nullable = false)
    private String name;

    @Column(name = "TYPE_DESCRIPTION", nullable = false)
    private String description;

    @OneToMany(mappedBy = "approvalTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalFlow> approvalFlows = new ArrayList<>(); 

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TYPE_ID")
    private List<ApprovalField> fields = new ArrayList<>(); 

    @Column(name = "CREATED_TS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_TS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
