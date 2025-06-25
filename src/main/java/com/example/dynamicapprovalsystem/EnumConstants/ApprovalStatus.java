package com.example.dynamicapprovalsystem.EnumConstants;

public enum ApprovalStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    RECALLED("Recalled"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");

    private final String value;

    ApprovalStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ApprovalStatus fromValue(String value) {
        for (ApprovalStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}   

