package com.example.dynamicapprovalsystem.DTOs;

import java.util.List;

import lombok.Data;

@Data
public class StatisticsResponse  {
    private List<ApplicationCounts> statistics;

    public StatisticsResponse(List<ApplicationCounts> statistics) {
        this.statistics = statistics;
    }
}
