package com.example.dynamicapprovalsystem.DTOs;

import lombok.Data;

@Data
public class ApprovalRequestFieldValueDTO {
    private Long id;
    private String fieldName;
    private String fieldValue;

    public ApprovalRequestFieldValueDTO(Long id, String fieldName, String fieldValue) {
        this.id = id;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}