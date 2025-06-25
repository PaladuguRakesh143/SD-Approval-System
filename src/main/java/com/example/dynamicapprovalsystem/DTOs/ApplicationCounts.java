package com.example.dynamicapprovalsystem.DTOs;

import lombok.Data;

@Data
    public class ApplicationCounts {
        private int total;
        private int completed;
        private int pending;
        private int rejected;

        public ApplicationCounts(int totalApplications, int completedApplications, int pendingApplications, int rejectedApplications) {
            this.total = totalApplications;
            this.completed = completedApplications;
            this.pending= pendingApplications;
            this.rejected = rejectedApplications;
        }
    }
    